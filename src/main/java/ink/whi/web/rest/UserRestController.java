package ink.whi.web.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.dto.FileDTO;
import ink.whi.api.model.enums.RoleEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.permission.Permission;
import ink.whi.api.permission.UserRole;
import ink.whi.core.util.JwtUtil;
import ink.whi.core.util.SessionUtil;
import ink.whi.service.file.FileDao;
import ink.whi.service.user.UserDao;
import ink.whi.service.user.entity.UserDO;
import ink.whi.web.global.GlobalInitHelper;
import ink.whi.web.vo.UserDetailVo;
import ink.whi.web.vo.UserSaveReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户接口
 *
 * @author: qing
 * @Date: 2023/5/7
 */
@RestController
@RequestMapping(path = "user")
public class UserRestController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FileDao fileDao;

    /**
     * 登录
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping(path = "login")
    public ResVo<BaseUserInfoDTO> login(HttpServletRequest request,
                                        HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "用户名或密码不能为空");
        }
        BaseUserInfoDTO info = userDao.passwordLogin(username, password);
        // 签发token
        String token = JwtUtil.createToken(info.getUserId());
        if (StringUtils.isNotBlank(token)) {
            Cookie cookie = SessionUtil.newCookie(GlobalInitHelper.SESSION_KEY, token);
            response.addCookie(cookie);
            return ResVo.ok(info);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "登录失败，请重试");
        }
    }

    /**
     * 登出接口
     *
     * @param response
     * @return
     */
    @GetMapping(path = "logout")
    public ResVo<String> logout(HttpServletResponse response) {
        response.addCookie(SessionUtil.delCookie(GlobalInitHelper.SESSION_KEY));
        return ResVo.ok("ok");
    }

    /**
     * 用户详情接口
     *
     * @param userId
     * @return
     */
    @GetMapping(path = "/{userId}")
    public ResVo<UserDetailVo> getUserInfo(@PathVariable(name = "userId") Long userId) {
        UserDetailVo vo = new UserDetailVo();
        BaseUserInfoDTO user = userDao.queryBasicUserInfo(userId);
        vo.setUserInfo(user);

        List<FileDTO> fileList = fileDao.listFileByUserId(userId);
        vo.setFileList(fileList);
        return ResVo.ok(vo);
    }

    /**
     * 创建用户(管理员用)
     *
     * @return
     */
    @Permission(role = UserRole.LEADER)
    @PostMapping(path = "save")
    public ResVo<String> saveUser(@RequestBody UserSaveReq req) {
        userDao.saveUser(req);
        return ResVo.ok("ok");
    }

    /**
     * 用户修改(普通用户使用)
     *
     * @param req
     * @return
     */
    @PostMapping(path = "update")
    public ResVo<String> updateUser(@RequestBody UserSaveReq req) {
        if (req.getPassword().length() < 6) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "密码不能少于6位");
        }
        userDao.updateUser(req);
        return ResVo.ok("ok");
    }

    /**
     * 根据token获取当前用户信息
     *
     * @return
     */
    @GetMapping(path = "info")
    public ResVo<BaseUserInfoDTO> getUserInfo() {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        BaseUserInfoDTO dto = userDao.queryBasicUserInfo(userId);
        return ResVo.ok(dto);
    }

    /**
     * 用户列表接口
     * todo：添加分页
     *
     * @return
     */
    @GetMapping(path = "list")
    public ResVo<List<BaseUserInfoDTO>> list() {
        List<BaseUserInfoDTO> userList = userDao.getUserList();
        return ResVo.ok(userList);
    }

    /**
     * 用户查询接口
     *
     * @param key
     * @return
     */
    @GetMapping(path = "seek")
    public ResVo<List<BaseUserInfoDTO>> search(@RequestParam(name = "key") String key) {
        List<BaseUserInfoDTO> list = userDao.queryUserBySearchKey(key);
        return ResVo.ok(list);
    }

    /**
     * 用户删除接口
     * @param userId
     * @return
     */
    @Permission(role = UserRole.LEADER)
    @GetMapping(path = "del/{userId}")
    public ResVo<String> deleteUser(@PathVariable Long userId) {
        userDao.deleteUser(userId);
        return ResVo.ok("ok");
    }
}
