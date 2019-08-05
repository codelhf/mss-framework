package com.mss.framework.base.core.token;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @Description: token工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 11:49
 */
@Slf4j
public class TokenUtil {
    // 加密方式
    private static final String secret = "HS256";
    // 签发者
    private static final String issuer = "adminIssuer";
    // jwt令牌
    private static final String AUTH_TOKEN = "X-AUTH-TOKEN";
    // 过期时间5分钟
    public static long EXPIRE_TIME = 5 * 60 * 1000L;
    // refreshToken过期时间
    public static long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;

    public static String accessToken(TokenUser tokenUser, Long expiresMillis) {
        if (tokenUser == null) {
            return null;
        }
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = EXPIRE_TIME;
        }
        String jsonUser = JSON.toJSONString(tokenUser);
        return JWTUtil.sign(jsonUser, secret, issuer, expiresMillis);
    }
    
    public static String refreshToken(String tokenId, Long expiresMillis) {
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = REFRESH_TOKEN_EXPIRE_TIME;
        }
        return JWTUtil.sign(tokenId, secret, issuer, expiresMillis);
    }

    public static TokenUser getTokenUser(HttpServletRequest request) {
        String authToken = request.getHeader(AUTH_TOKEN);
        if (StringUtils.isBlank(authToken)) {
            return null;
        }
        String jsonUser = JWTUtil.verify(authToken, secret, issuer);
        if (StringUtils.isBlank(jsonUser)) {
            return null;
        }
        return JSON.parseObject(jsonUser, TokenUser.class);
    }

    public static String getTokenId(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return null;
        }
        return JWTUtil.verify(refreshToken, secret, issuer);
    }

    public static String sign(TokenUser tokenUser, Long expiresIn, TimeUnit timeUnit) {
        if (tokenUser == null) {
            return null;
        }
        if (expiresIn == null || expiresIn < 0) {
            expiresIn = EXPIRE_TIME;
        } else {
            expiresIn = timeUnit.toMillis(expiresIn);
        }
        return JWTUtil.sign(JSON.toJSONString(tokenUser), secret, issuer, expiresIn);
    }

    public static TokenUser verify(String tokenStr) {
        String jsonUser = JWTUtil.verify(tokenStr, secret, issuer);
        if (StringUtils.isBlank(jsonUser)) {
            return null;
        }
        return JSON.parseObject(jsonUser, TokenUser.class);
    }
}
