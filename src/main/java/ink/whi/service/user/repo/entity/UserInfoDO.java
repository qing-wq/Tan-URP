package ink.whi.service.user.repo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author: qing
 * @Date: 2023/5/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfoDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 5419872802522176653L;

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 姓名
     */
    private String userInfoName;
    /**
     * 学号
     */
    private String studentId;
    /**
     * 用户角色
     */
    private Integer userRole;
    /**
     * 年级
     */
    private String grade;
    /**
     * 是否删除
     */
    private Integer deleted;
}
