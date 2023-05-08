package ink.whi.web.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.util.JwtUtil;
import ink.whi.service.meeting.repo.MeetingDao;
import ink.whi.service.user.repo.UserDao;
import ink.whi.web.vo.IndexVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ink.whi.web.global.GlobalInitHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 首页接口
 *
 * @author: qing
 * @Date: 2023/5/7
 */
@RestController
public class IndexRestController {

    @Autowired
    private MeetingDao meetingDao;

    @Autowired
    private UserDao userDao;

    /**
     * 首页访问接口
     *
     * @return
     */
    @GetMapping(path = {"/", "/index"})
    public ResVo<IndexVo> index() {
        IndexVo vo = new IndexVo();
        PageParam pageParam = PageParam.newPageInstance();
        // 会议列表
        PageListVo<BaseMeetingDTO> list = meetingDao.listMeetings(pageParam);
        vo.setMeetingList(list);

        // 查询用户资料
        if (ReqInfoContext.getReqInfo().getUserId() != null) {
            BaseUserInfoDTO userInfo = userDao.queryBasicUserInfo(ReqInfoContext.getReqInfo().getUserId());
            vo.setUserInfo(userInfo);
        }
        return ResVo.ok(vo);
    }

    /**
     * 登录
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
            response.addCookie(new Cookie(GlobalInitHelper.SESSION_KEY, token));
            return ResVo.ok(info);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "登录失败，请重试");
        }
    }
}
