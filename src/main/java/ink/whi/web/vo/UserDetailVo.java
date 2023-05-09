package ink.whi.web.vo;

import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.dto.FileDTO;
import lombok.Data;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/9
 */
@Data
public class UserDetailVo {
    /**
     * 用户信息
     */
    private BaseUserInfoDTO userInfo;
    /**
     * 用户上传的所有文件列表
     */
    private List<FileDTO> fileList;
}
