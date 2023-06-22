package ink.whi.service.converter;

import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.MeetingSaveReq;
import ink.whi.service.meeting.MeetingDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 实体转换器
 * @author: qing
 * @Date: 2023/5/7
 */
public class MeetingConverter {

    public static BaseMeetingDTO toDto(MeetingDO meetingDO) {
        BaseMeetingDTO dto = new BaseMeetingDTO();
        dto.setMeetingId(meetingDO.getId());
        BeanUtils.copyProperties(meetingDO, dto);
        return dto;
    }

    public static List<BaseMeetingDTO> toDtoList(List<MeetingDO> list) {
        return list.stream().map(MeetingConverter::toDto).toList();
    }

    public static MeetingDO toDO(MeetingSaveReq meeting) {
        MeetingDO meetingDO = new MeetingDO();
        BeanUtils.copyProperties(meeting, meetingDO);
        return meetingDO;
    }
}
