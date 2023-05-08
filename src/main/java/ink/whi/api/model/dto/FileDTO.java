package ink.whi.api.model.dto;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/5/8
 */
@Data
public class FileDTO {

    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 用户信息
     */
    private BaseUserInfoDTO userInfo;
}
