package com.mss.framework.base.core.token;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: token工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 11:49
 */
@Slf4j
public class TokenUtil {

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
        return JWTUtil.sign(jsonUser, expiresMillis);
    }
    
    public static String refreshToken(String userId, Long expiresMillis) {
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = EXPIRE_TIME;
        }
        return JWTUtil.sign(userId, expiresMillis);
    }

    public static TokenUser getTokenUser(HttpServletRequest request) {
        String authToken = request.getHeader(AUTH_TOKEN);
        if (StringUtils.isBlank(authToken)) {
            return null;
        }
        String jsonUser = JWTUtil.verify(authToken);
        if (StringUtils.isBlank(jsonUser)) {
            return null;
        }
        return JSON.parseObject(jsonUser, TokenUser.class);
    }

    public static String getUserId(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return null;
        }
        return JWTUtil.verify(refreshToken);
    }
}
