package ink.whi.api.model.enums;

import lombok.Getter;

/**
 * 状态的枚举
 */
@Getter
public enum YesOrNoEnum {

    NO(0, "否"),
    YES(1,"是" );

    YesOrNoEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final int code;
    private final String desc;
}
