package ink.whi.service.meeting.converter;

import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.service.meeting.repo.MeetingDO;
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
        BeanUtils.copyProperties(meetingDO, dto);
        return dto;
    }

    public static List<BaseMeetingDTO> toDtoList(List<MeetingDO> list) {
        return list.stream().map(MeetingConverter::toDto).toList();
    }
}
