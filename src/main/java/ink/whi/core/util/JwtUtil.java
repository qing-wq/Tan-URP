package ink.whi.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * JWT工具类
 * @author: qing
 * @Date: 2023/4/26
 */
@Slf4j
@Data
public class JwtUtil {
    private static String JWT_KEY = "JWT_KEY";
    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String Authorization = "Authorization";
    private static final long ONE_MONTH = 30 * 24 * 60 * 60 * 1000L;

    /**
     * 生成token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + ONE_MONTH))
                .sign(algorithm);
    }

    /**
     * 校验token
     *
     * @param token
     * @return userId
     */
    public static Long isVerify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
            String userId = JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
            return Long.parseLong(userId);
        } catch (JWTVerificationException e) {
            log.error("[Error]: token verify error: " + e.getMessage());
            throw BusinessException.newInstance(StatusEnum.JWT_VERIFY_EXISTS);
        }
    }

    /**
     * 检验token是否需要更新
     *
     * @param token
     * @return
     */
    public static boolean isNeedUpdate(String token) {
        Date expiresAt = JWT.require(Algorithm.HMAC256(JWT_KEY))
                .build()
                .verify(token)
                .getExpiresAt();
        // 小于半个月更新
        return (expiresAt.getTime() - System.currentTimeMillis()) < (ONE_MONTH >> 1);
    }

}
