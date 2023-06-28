package ink.whi.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.enums.RoleEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.core.util.UserPwdEncoder;
import ink.whi.service.converter.UserConverter;
import ink.whi.service.user.entity.UserDO;
import ink.whi.service.user.entity.UserInfoDO;
import ink.whi.web.vo.UserSaveReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Slf4j
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
        UserDO user = queryByUserName(username);
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, username);
        }
        if (!userPwdEncoder.match(password, user.getPassWord())) {
            throw BusinessException.newInstance(StatusEnum.USER_PWD_ERROR);
        }
        return queryBasicUserInfo(user.getId());
    }

    private UserDO queryByUserName(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getUserName, username)
                .eq(UserDO::getDeleted, YesOrNoEnum.NO.getCode());
        return userMapper.selectOne(wrapper);
    }

    /**
     * 保存或更新用户（管理员）
     *
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long saveUser(UserSaveReq req) {
        UserDO user = new UserDO();
        // 默认账号密码为 学号：学号
        user.setUserName(req.getStudentId());
        String pwd = req.getPassword() == null ? req.getStudentId() : req.getPassword();
        user.setPassWord(userPwdEncoder.encode(pwd));
        UserInfoDO userInfo = UserConverter.toDo(req);

        if (req.getUserId() == null) {
            // 创建用户
            UserDO record = queryByUserName(req.getStudentId());
            if (record != null) {
                // 每个学号只能创建一个用户
                throw BusinessException.newInstance(StatusEnum.USER_ALREADY_EXISTS);
            }
            userMapper.insert(user);
            Long userId = user.getId();
            userInfo.setUserId(userId);
            save(userInfo);
            return userId;
        } else {
            // 更新用户
            Long userId = req.getUserId();
            user.setId(userId);
            userMapper.updateById(user);
            userInfo.setUserId(userId);
            updateByUserId(userInfo);
            return userId;
        }
    }

    /**
     * 更新用户（普通用户）
     *
     * @param req
     */
    public void updateUser(UserSaveReq req) {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        UserDO record = queryUserByUserId(userId);
        if (record == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, "账号不存在，请联系管理员");
        }
        record.setPassWord(userPwdEncoder.encode(req.getPassword()));
        userMapper.updateById(record);
    }

    private void updateByUserId(UserInfoDO userInfo) {
        UserInfoDO record = lambdaQuery().eq(UserInfoDO::getUserId, userInfo.getUserId())
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .list().get(0);
        userInfo.setId(record.getId());
        updateById(userInfo);
    }

    public List<BaseUserInfoDTO> getUserList() {
        List<UserInfoDO> list = lambdaQuery().eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByAsc(UserInfoDO::getStudentId)
                .list();
        return UserConverter.toDtoList(list);
    }

    public List<BaseUserInfoDTO> queryUserBySearchKey(String key) {
        // 先查userInfoName
        List<UserInfoDO> list = lambdaQuery().eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .and(StringUtils.isNotBlank(key),
                        v -> v.like(UserInfoDO::getUserInfoName, key)
                                .or()
                                .like(UserInfoDO::getStudentId, key))
                .orderByDesc(UserInfoDO::getUserId)
                .list();
        return UserConverter.toDtoList(list);
    }

    public UserDO queryUserByUserId(Long userId) {
        UserDO record = userMapper.selectById(userId);
        if (record == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, userId);
        }
        return record;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // user + user_info
        UserInfoDO record = queryInfoByUserId(userId);
        if (record != null) {
            if (record.getUserRole() == RoleEnum.ADMIN.getRole()) {
                throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "操作非法：" + userId);
            }
            log.warn("[WARN] 用户：" + ReqInfoContext.getReqInfo().getUserId() + " 删除了用户：" + userId);
            record.setDeleted(YesOrNoEnum.YES.getCode());
            updateById(record);
        }

        UserDO userRecord = queryUserByUserId(userId);
        userRecord.setDeleted(YesOrNoEnum.YES.getCode());
        userMapper.updateById(userRecord);
    }

    private UserInfoDO queryInfoByUserId(Long userId) {
        return lambdaQuery().eq(UserInfoDO::getUserId, userId)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .one();
    }
}
