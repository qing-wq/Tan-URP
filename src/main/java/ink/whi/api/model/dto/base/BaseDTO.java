package ink.whi.api.model.dto.base;

import lombok.Data;

import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/6
 */
@Data
public class BaseDTO {
    private Long id;

    private Date createTime;

    private Date updateTime;
}
