package ink.whi.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 密码加盐校验工具类
 * @author: qing
 * @Date: 2023/4/26
 */
@Component
public class UserPwdEncoder {
    /**
     * 盐值
     */
    @Value("${security.salt}")
    private String salt;

    public boolean match(String plainPwd, String encPwd) {
        return Objects.equals(DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8)), encPwd);
    }

}