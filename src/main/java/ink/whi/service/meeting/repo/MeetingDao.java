package ink.whi.service.meeting.repo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.dto.base.BaseDO;
import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.MeetingSaveReq;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.service.converter.MeetingConverter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Repository
public class MeetingDao extends ServiceImpl<MeetingMapper, MeetingDO> {
    /**
     * 查询全部meeting
     * @param pageParam
     * @return
     */
    public PageListVo<BaseMeetingDTO> listMeetings(PageParam pageParam) {
        List<MeetingDO> list = lambdaQuery().eq(MeetingDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(BaseDO::getCreateTime)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return PageListVo.emptyVo();
        }
        return PageListVo.newVo(MeetingConverter.toDtoList(list), pageParam.getPageSize());
    }

    /**
     * 通过Tag获取Meeting
     * @param tag
     * @param pageParam
     * @return
     */
    public PageListVo<BaseMeetingDTO> listMeetingByTag(Integer tag, PageParam pageParam) {
        List<MeetingDO> list = lambdaQuery().eq(MeetingDO::getTag, tag)
                .eq(MeetingDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(BaseDO::getCreateTime)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return PageListVo.emptyVo();
        }
        return PageListVo.newVo(MeetingConverter.toDtoList(list), pageParam.getPageSize());
    }

    /**
     * 保存或更新会议
     * @param meeting
     */
    public Long saveMeeting(MeetingSaveReq meeting) {
        MeetingDO meetingDO = MeetingConverter.toDO(meeting);
        if (meetingDO.getId() == null) {
            save(meetingDO);
        } else {
            updateById(meetingDO);
        }
        return meetingDO.getId();
    }

    /**
     * 删除会议
     * @param meetingId
     */
    public void deleteMeeting(Long meetingId) {
        MeetingDO record = getById(meetingId);
        if (record == null) {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, meetingId);
        }
        record.setDeleted(YesOrNoEnum.YES.getCode());
        updateById(record);
    }
}
