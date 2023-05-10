package ink.whi.api.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Data
public class BaseMeetingDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8517593099569358555L;
    /**
     * 业务主键
     */
    private Long meetingId;
    /**
     * 组会名称
     */
    private String meetName;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 会议地点
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String location;
    /**
     * 组会内容
     */
    private String content;
    /**
     * 会议主题
     */
    private String subject;
    /**
     * 1-大一 2-大二 3-大三 4-大四 0-研究生
     */
    private Integer tag;
    /**
     * 发表人姓名
     */
    private String publisher;
}
