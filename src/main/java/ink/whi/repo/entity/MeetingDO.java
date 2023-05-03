package ink.whi.repo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@TableName("meeting")
public class MeetingDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 3180436943478287550L;

    private String name;

    private String content;

    private String location;

    private Date beginTime;

    private Date endTime;
}
