package ink.whi.service.meeting;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.MeetingSaveReq;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.service.converter.MeetingConverter;
import ink.whi.service.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Repository
public class MeetingDao extends ServiceImpl<MeetingMapper, MeetingDO> {

    @Autowired
    private UserDao userDao;

    /**
     * 查询全部meeting
     *
     * @param pageParam
     * @return
     */
    public PageListVo<BaseMeetingDTO> listMeetings(PageParam pageParam) {
        List<MeetingDO> list = lambdaQuery().eq(MeetingDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(MeetingDO::getBeginTime)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return PageListVo.emptyVo();
        }
        return PageListVo.newVo(buildMeetingDtoList(list), pageParam.getPageSize());
    }

    private List<BaseMeetingDTO> buildMeetingDtoList(List<MeetingDO> list) {
        return list.stream().map(this::fillMeetingDto).toList();
    }

    /**
     * 补充发布者信息
     * @param meetingDO
     * @return
     */
    private BaseMeetingDTO fillMeetingDto(MeetingDO meetingDO) {
        BaseMeetingDTO dto = MeetingConverter.toDto(meetingDO);
        BaseUserInfoDTO user = userDao.queryBasicUserInfo(meetingDO.getPublisher());
        dto.setPublisher(user);
        return dto;
    }

    /**
     * 通过Tag获取Meeting
     *
     * @param tag
     * @param pageParam
     * @return
     */
    public PageListVo<BaseMeetingDTO> listMeetingByTag(Integer tag, PageParam pageParam) {
        List<MeetingDO> list = lambdaQuery().eq(MeetingDO::getTag, tag)
                .eq(MeetingDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(MeetingDO::getBeginTime)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return PageListVo.emptyVo();
        }
        return PageListVo.newVo(buildMeetingDtoList(list), pageParam.getPageSize());
    }

    /**
     * 保存或更新会议
     *
     * @param meeting
     */
    public Long saveMeeting(MeetingSaveReq meeting) {
        MeetingDO meetingDO = MeetingConverter.toDO(meeting);
        if (meeting.getMeetingId() == null) {
            // 保存会议
            userDao.queryByUserId(meeting.getPublisher());  // 查询publisher是否存在
            if (meeting.getContent() == null) {
                // 设置会议内容默认值
                meetingDO.setContent("");
            }
            save(meetingDO);
        } else {
            // 更新会议
            MeetingDO record = getById(meeting.getMeetingId());
            if (record == null) {
                throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, meeting.getMeetingId());
            }
            if (!Objects.equals(record.getPublisher(), ReqInfoContext.getReqInfo().getUserId())) {
                // 只有发布者能修改会议
                throw BusinessException.newInstance(StatusEnum.FORBID_ERROR);
            }
            meetingDO.setId(meeting.getMeetingId());
            if (meetingDO.getContent() == null) {
                // 如果修改后的内容为空，则不修改内容
                meetingDO.setContent(record.getContent());
            }
            updateById(meetingDO);
        }
        return meetingDO.getId();
    }

    /**
     * 删除会议
     *
     * @param meetingId
     */
    public void deleteMeeting(Long meetingId) {
        MeetingDO record = getById(meetingId);
        if (record == null) {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, meetingId);
        }
        if (!Objects.equals(record.getPublisher(), ReqInfoContext.getReqInfo().getUserId())) {
            // 只有发布者能修改会议
            throw BusinessException.newInstance(StatusEnum.FORBID_ERROR);
        }
        record.setDeleted(YesOrNoEnum.YES.getCode());
        updateById(record);
    }

    public BaseMeetingDTO queryByMeetingId(Long meetingId) {
        MeetingDO meeting = getById(meetingId);
        if (meeting == null || meeting.getDeleted() == YesOrNoEnum.YES.getCode()) {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, meetingId);
        }
        return fillMeetingDto(meeting);
    }
}
