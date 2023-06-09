package ink.whi.web.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.service.meeting.MeetingDao;
import ink.whi.service.user.UserDao;
import ink.whi.web.vo.IndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
