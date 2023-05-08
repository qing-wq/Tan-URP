package ink.whi.service.user.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.service.user.repo.entity.UserDO;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Component
public interface UserMapper extends BaseMapper<UserDO> {
}
