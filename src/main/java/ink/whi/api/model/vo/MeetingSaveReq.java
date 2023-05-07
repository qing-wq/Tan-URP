package ink.whi.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MeetingSaveReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 5384017856429978434L;

    /**
     * 会议ID
     */
    private Long meetingId;
    /**
     * 组会名称
     */
    private String meetName;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endTime;
    /**
     * 会议地点
     */
    private String location;
    /**
     * 组会内容
     */
    private String content;
    /**
     * 1-大一 2-大二 3-大三 4-大四 0-研究生
     */
    private Integer tag;
}
