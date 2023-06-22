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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long saveUser(UserSaveReq req) {
        // user + user_info
        String role = RoleEnum.role(req.getUserRole()).name();
        if (Objects.equals(role, RoleEnum.ADMIN.name())) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "操作非法：" + req.getUserRole());
        }

        UserDO user = new UserDO();
        // 默认账号密码为 学号：学号
        user.setUserName(req.getUserName() == null ? req.getStudentId() : req.getUserName());
        String pwd = req.getPassword() == null ? req.getStudentId() : req.getPassword();
        user.setPassWord(userPwdEncoder.encode(pwd));

        // 校验用户是否存在
        UserDO record = queryByUserName(user.getUserName());
        if (record == null) {
            // 用户不存在则创建
            userMapper.insert(user);
            UserInfoDO userInfo = UserConverter.toDo(req);
            userInfo.setUserId(user.getId());
            save(userInfo);
            return userInfo.getId();
        }else {
            // 用户存在则更新
            record.setPassWord(user.getPassWord());
            userMapper.updateById(user);
            UserInfoDO userInfo = UserConverter.toDo(req);
            userInfo.setUserId(record.getId());
            return updateByUserId(userInfo);
        }
    }

    private Long updateByUserId(UserInfoDO userInfo) {
        UserInfoDO record = lambdaQuery().eq(UserInfoDO::getUserId, userInfo.getUserId())
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .list().get(0);
        userInfo.setId(record.getId());
        updateById(userInfo);
        return userInfo.getId();
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

    /**
     * 更新用户（普通用户）
     * @param req
     */
    public void updateUser(UserSaveReq req) {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        UserDO record = queryByUserId(userId);
        if (record == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, "账号不存在，请联系管理员");
        }
        record.setPassWord(userPwdEncoder.encode(req.getPassword()));
        userMapper.updateById(record);
    }

    public UserDO queryByUserId(Long userId) {
        UserDO record = userMapper.selectById(userId);
        if (record == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, userId);
        }
        return record;
    }
}
