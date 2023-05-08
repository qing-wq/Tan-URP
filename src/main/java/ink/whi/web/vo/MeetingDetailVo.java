package ink.whi.web.vo;

import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.dto.FileDTO;
import lombok.Data;

/**
 * 会议详情
 * @author: qing
 * @Date: 2023/5/8
 */
@Data
public class MeetingDetailVo {

    private BaseMeetingDTO meetingDTO;

    private FileDTO fileDTO;
}
