package ink.whi.api.model.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Getter
public enum TagTypeEnum {

    GRADUATE(0, "研究生"),
    ONE(1, "大一"),
    TWO(2, "大二"),
    THREE(3, "大三"),
    FOUR(4, "大四")
    ;

    private final Integer code;
    private final String desc;

    TagTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TagTypeEnum formCode(Integer code) {
        for (TagTypeEnum value : TagTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
