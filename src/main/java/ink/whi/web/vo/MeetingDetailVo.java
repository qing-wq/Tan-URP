package ink.whi.web.vo;

import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.dto.FileDTO;
import lombok.Data;

import java.util.List;

/**
 * 会议详情
 * @author: qing
 * @Date: 2023/5/8
 */
@Data
public class MeetingDetailVo {
    /**
     * 会议信息
     */
    private BaseMeetingDTO meetingDTO;

    /**
     * 文件列表
     */
    private List<FileDTO> fileList;
}
