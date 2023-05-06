package ink.whi.service.user.repo.entity;

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
@TableName("user")
public class UserDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = -6744486127264287373L;

    /**
     * 账号
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 是否删除
     */
    private String deleted;
}
