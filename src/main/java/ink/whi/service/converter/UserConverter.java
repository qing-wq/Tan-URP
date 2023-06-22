package ink.whi.service.converter;

import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.dto.UserRoleDTO;
import ink.whi.api.model.enums.RoleEnum;
import ink.whi.service.user.entity.UserInfoDO;
import ink.whi.web.vo.UserSaveReq;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 用户实体转换器
 * @author: qing
 * @Date: 2023/5/7
 */
@Data
public class UserConverter {
    public static BaseUserInfoDTO toDto(UserInfoDO info) {
        if (info == null) {
            return null;
        }
        BaseUserInfoDTO user = new BaseUserInfoDTO();
        BeanUtils.copyProperties(info, user);
        RoleEnum roleEnum = RoleEnum.role(info.getUserRole());
        user.setRole(new UserRoleDTO(roleEnum));
        return user;
    }

    public static UserInfoDO toDo(UserSaveReq req) {
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(req, userInfoDO);
        return userInfoDO;
    }

    public static List<BaseUserInfoDTO> toDtoList(List<UserInfoDO> list) {
        return list.stream().map(UserConverter::toDto).toList();
    }
}
