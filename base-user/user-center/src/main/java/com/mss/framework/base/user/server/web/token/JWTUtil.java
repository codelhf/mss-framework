package com.mss.framework.base.user.server.web.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mss.framework.base.core.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

/**
 * @Description: jwt工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 8:34
 */
@Configuration
@Slf4j
public class JWTUtil {

    @Value("${token.jwt.secret:123456}")
    private String secret;
    @Value("${token.jwt.issuer:123456}")
    private String issuer;

    /**
     * 生成签名,默认5min后过期
     *
     * @param userInfo 用户名
     * @param expiresMillis   用户的密码
     * @return 加密的token
     */
    public String sign(String userInfo, Long expiresMillis) {
        try {
            //过期时间
            Date expiresTime = new Date(System.currentTimeMillis() + expiresMillis);
            //加密方式
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //生成秘钥
            return JWT.create()
                    .withHeader(new HashMap<>())
                    .withAudience()
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withSubject(userInfo)
                    .withExpiresAt(expiresTime)
                    .withNotBefore(new Date())
                    .withJWTId(IDUtil.UUIDStr())
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @return DecodedJWT 验证后的令牌
     */
    public String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null) {
                return null;
            }
            return decodedJWT.getSubject();
        } catch (Exception exception) {
            return null;
        }
    }
}
