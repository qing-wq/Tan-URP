package ink.whi.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.enums.RoleEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.core.util.SpringUtil;
import ink.whi.core.util.UserPwdEncoder;
import ink.whi.service.converter.UserConverter;
import ink.whi.service.user.entity.UserDO;
import ink.whi.service.user.entity.UserInfoDO;
import ink.whi.web.vo.UserSaveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Repository
public class UserDao extends ServiceImpl<UserInfoMapper, UserInfoDO> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPwdEncoder userPwdEncoder;

    public BaseUserInfoDTO queryBasicUserInfo(Long userId) {
        UserInfoDO user = lambdaQuery().eq(UserInfoDO::getUserId, userId)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .one();
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, userId);
        }
        return UserConverter.toDto(user);
    }

    public BaseUserInfoDTO passwordLogin(String username, String password) {
        UserDO user = getByUserName(username);
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, username);
        }
        if (!userPwdEncoder.match(password, user.getPassWord())) {
            throw BusinessException.newInstance(StatusEnum.USER_PWD_ERROR);
        }
        return queryBasicUserInfo(user.getId());
    }

    private UserDO getByUserName(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getUserName, username)
                .eq(UserDO::getDeleted, YesOrNoEnum.NO.getCode());
        return userMapper.selectOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveUser(UserSaveReq req) {
        // user + user_info
        String role = RoleEnum.role(req.getUserRole());
        if (role == null || Objects.equals(role, RoleEnum.TAN.name())) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "操作非法：" + req.getUserRole());
        }

        UserDO user = new UserDO();
        // 默认账号密码
        user.setUserName(req.getUserName() == null ? req.getStudentId() : req.getUserName());
        user.setPassWord(req.getPassword() == null ? SpringUtil.getConfig("password.default." + role.toLowerCase()) : req.getPassword());

        // 校验用户是否存在
        UserDO record = getByUserName(user.getUserName());
        if (record != null) {
            throw BusinessException.newInstance(StatusEnum.USER_ALREADY_EXISTS);
        }
        userMapper.insert(user);

        UserInfoDO userInfo = UserConverter.toDo(req);
        userInfo.setUserId(user.getId());
        save(userInfo);
        return userInfo.getId();
    }
}
