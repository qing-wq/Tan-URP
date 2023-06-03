package ink.whi.service.meeting;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import ink.whi.api.model.dto.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("meeting")
public class MeetingDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -8915384579444426238L;
    /**
     * 组会名称
     */
    private String meetName;
    /**
     * 开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 结束时间
     */
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
     * 会议主题
     */
    private String subject;
    /**
     * 1-大一 2-大二 3-大三 4-大四 0-研究生
     */
    private Integer tag;
    /**
     * 发表人ID
     */
    private Long publisher;
    /**
     * 是否删除
     */
    private Integer deleted;
}
