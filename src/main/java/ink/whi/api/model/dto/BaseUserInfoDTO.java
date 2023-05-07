package ink.whi.api.model.dto;

import ink.whi.api.model.base.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Data
@Accessors(chain = true)
public class BaseUserInfoDTO extends BaseDTO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userInfoName;
    /**
     * 学号
     */
    private String studentId;

    /**
     * 用户角色
     */
    private String role;
    /**
     * 年级
     */
    private String grade;
}
