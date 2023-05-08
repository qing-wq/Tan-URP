package ink.whi.api.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Data
@Accessors(chain = true)
public class BaseUserInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1854535202157642028L;
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
