package ink.whi.service.meeting.repo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

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

    @Id
    private String id;

    private Date createTime;

    private Date updateTime;
}
