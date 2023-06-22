package ink.whi.api.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Getter
public enum RoleEnum {
    NORMAL(0, "普通用户"),
    LEADER(1, "组长"),
    TAN(2, "谭sir")
    ;

    private int role;
    private String desc;

    RoleEnum(int role, String desc) {
        this.role = role;
        this.desc = desc;
    }

    public static RoleEnum role(Integer roleId) {
        for (RoleEnum value : RoleEnum.values()) {
            if (Objects.equals(roleId, value.getRole())) {
                return value;
            }
        }
        return RoleEnum.NORMAL;
    }
}
