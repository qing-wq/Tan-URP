package ink.whi.api.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/8
 */
@Data
public class FileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8392092851005161008L;
    /**
     * 文件ID
     */
    private Long fileId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 会议ID
     */
    private Long meetId;
    /**
     * 下载次数
     */
    private Integer download;
    /**
     * 文件大小(Byte)
     */
    private Long fileSize;
    /**
     * 用户信息
     */
    private BaseUserInfoDTO userInfo;
}
