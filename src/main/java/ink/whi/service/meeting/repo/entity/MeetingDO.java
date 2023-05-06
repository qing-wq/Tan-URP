package ink.whi.service.meeting.repo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "urp")
public class MeetingDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 3180436943478287550L;

    private String name;

    private String content;

    private String location;

    private Date beginTime;

    private Date endTime;
}
