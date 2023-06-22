package ink.whi.api.model.dto;

import ink.whi.api.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/6/20
 */
@Data
public class UserRoleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5922022813681455476L;

    private Integer roleId;

    private String roleName;

    public UserRoleDTO() {
    }

    public UserRoleDTO(RoleEnum roleEnum) {
        this.roleId = roleEnum.getRole();
        this.roleName = roleEnum.getDesc();
    }
}
