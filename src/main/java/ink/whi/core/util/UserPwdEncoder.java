package ink.whi.core.util;

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
        plainPwd = plainPwd + salt;
        return Objects.equals(DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8)), encPwd);
    }

    public String encode(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

}