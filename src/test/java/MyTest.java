import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
public class MyTest {
    @Test
    public void test() {
        String plainPwd = "123456";
        System.out.println(DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8)));
    }
}
