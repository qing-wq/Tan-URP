package ink.whi.web.rest;

import ink.whi.api.model.dto.base.PageHelper;
import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.enums.TagTypeEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.MeetingSaveReq;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.permission.Permission;
import ink.whi.api.permission.UserRole;
import ink.whi.service.converter.MeetingConverter;
import ink.whi.service.meeting.repo.MeetingDO;
import ink.whi.service.meeting.repo.MeetingDao;
import ink.whi.api.model.vo.ResVo;
import ink.whi.web.vo.MeetingDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会议接口
 * @author: qing
 * @Date: 2023/5/3
 */
@Slf4j
@RestController
@RequestMapping(path = "meeting")
public class MeetingRestController extends PageHelper {

    @Autowired
    private MeetingDao meetingDao;

    /**
     * 会议列表分页接口
     * @return
     */
    @GetMapping(path = "list")
    public ResVo<PageListVo<BaseMeetingDTO>> list(@RequestParam(name = "page") Long pageNum,
                                             @RequestParam(name = "pageSize") Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        PageListVo<BaseMeetingDTO> list = meetingDao.listMeetings(pageParam);
        return ResVo.ok(list);
    }

    /**
     * 根据标签查询组会记录
     * @param tag
     * @return
     */
    @GetMapping(path = "tags/{tag}")
    public ResVo<PageListVo<BaseMeetingDTO>> getTags(@PathVariable(name = "tag") Integer tag,
                                                     @RequestParam(name = "page") Long pageNum,
                                                     @RequestParam(name = "pageSize") Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        TagTypeEnum type = TagTypeEnum.formCode(tag);
        if (type == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "操作非法: " + tag);
        }
        PageListVo<BaseMeetingDTO> list = meetingDao.listMeetingByTag(tag, pageParam);
        return ResVo.ok(list);
    }

    /**
     * 保存会议
     * @param meeting
     * @return
     */
    @PostMapping(path = "save")
    public ResVo<Long> saveMeeting(@RequestBody MeetingSaveReq meeting) {
        Long meetingId = meetingDao.saveMeeting(meeting);
        return ResVo.ok(meetingId);
    }

    /**
     * 会议删除接口
     * @param meetingId
     * @return
     */
    @Permission(role = UserRole.LEADER)
    @GetMapping(path = "delete/{meetingId}")
    public ResVo<String> deleteMeeting(@PathVariable(name = "meetingId") Long meetingId) {
        meetingDao.deleteMeeting(meetingId);
        return ResVo.ok("ok");
    }

    @GetMapping(path = "detail/{meetingId}")
    public ResVo<MeetingDetailVo> detail(@PathVariable(name = "meetingId") String meetingId) {
        MeetingDetailVo vo = new MeetingDetailVo();
        MeetingDO meeting = meetingDao.getById(meetingId);
        BaseMeetingDTO dto = MeetingConverter.toDto(meeting);
        vo.setMeetingDTO(dto);

        return ResVo.ok(vo);
    }
}
