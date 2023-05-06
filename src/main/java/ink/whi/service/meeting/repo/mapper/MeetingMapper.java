package ink.whi.service.meeting.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.service.meeting.repo.entity.MeetingDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/3
 */
public interface MeetingMapper extends BaseMapper<MeetingDO> {
}
