package ink.whi.service.file;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.dto.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author: qing
 * @Date: 2023/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("file")
public class FileDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = -340565077121920717L;

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 会议ID
     */
    private Long meetId;
}
