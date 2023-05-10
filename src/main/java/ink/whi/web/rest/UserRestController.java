package ink.whi.web.rest;

import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.dto.FileDTO;
import ink.whi.api.model.enums.RoleEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.permission.Permission;
import ink.whi.api.permission.UserRole;
import ink.whi.api.util.JwtUtil;
import ink.whi.service.file.FileDao;
import ink.whi.service.user.repo.UserDao;
import ink.whi.web.global.GlobalInitHelper;
import ink.whi.web.vo.UserDetailVo;
import ink.whi.web.vo.UserSaveReq;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

import org.springframework.boot.web.server.Cookie.SameSite;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

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
            Cookie cookie = new Cookie(GlobalInitHelper.SESSION_KEY, token);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResVo.ok(info);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "登录失败，请重试");
        }
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
     * 创建用户
     *
     * @return
     */
    @Permission(role = UserRole.LEADER)
    @PostMapping(path = "save")
    public ResVo<String> saveUser(@RequestBody UserSaveReq req) {
        userDao.saveUser(req);
        return ResVo.ok("ok");
    }
}
