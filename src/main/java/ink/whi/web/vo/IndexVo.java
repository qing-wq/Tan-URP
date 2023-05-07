package ink.whi.web.vo;

import ink.whi.api.model.dto.BaseMeetingDTO;
import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.PageListVo;
import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Data
public class IndexVo {
    /**
     * 会议列表
     */
    PageListVo<BaseMeetingDTO> meetingList;
    /**
     * 用户信息
     */
    private BaseUserInfoDTO userInfo;
}
