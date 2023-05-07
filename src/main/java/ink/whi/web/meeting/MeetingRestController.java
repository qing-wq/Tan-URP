package ink.whi.web.meeting;

import ink.whi.api.model.base.PageHelper;
import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.service.meeting.repo.MeetingDO;
import ink.whi.service.meeting.repo.MeetingDao;
import ink.whi.api.model.vo.ResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会议接口
 * @author: qing
 * @Date: 2023/5/3
 */
@Slf4j
@RestController
@RequestMapping(path = "meeting/api")
public class MeetingRestController extends PageHelper {

    @Autowired
    private MeetingDao meetingDao;

    /**
     * 会议列表分页接口
     * @return
     */
    @GetMapping(path = "list")
    public ResVo<PageListVo<BaseMeetingDTO>> getMeeting(@RequestParam(name = "page") Long pageNum,
                                             @RequestParam(name = "pageSize") Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        PageListVo<BaseMeetingDTO> list = meetingDao.listMeetings(pageParam);
        return ResVo.ok(list);
    }

}
