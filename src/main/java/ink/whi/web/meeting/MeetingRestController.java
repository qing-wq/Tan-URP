package ink.whi.web.meeting;

import ink.whi.api.model.base.PageHelper;
import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.enums.TagTypeEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.service.meeting.repo.MeetingDao;
import ink.whi.api.model.vo.ResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
