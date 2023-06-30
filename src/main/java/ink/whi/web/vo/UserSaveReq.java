package ink.whi.web.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/5/9
 */
@Data
public class UserSaveReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 793902321352322861L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 账号（不填默认为学号）
     */
    private String userName;
    /**
     * 密码（不填为默认密码）
     */
    private String password;
    /**
     * 姓名
     */
    private String userInfoName;
    /**
     * 学号
     */
    private String studentId;
    /**
     * 用户角色 0-normal 1-leader
     */
    private Integer userRole;
    /**
     * 年级
     */
    private String grade;
}
