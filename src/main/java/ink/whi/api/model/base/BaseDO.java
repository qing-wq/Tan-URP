package ink.whi.api.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@Data
public class BaseDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3648012732645150344L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;
}
