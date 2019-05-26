package com.mss.framework.base.user.server.web.token;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: token工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 11:49
 */
@Slf4j
public class TokenUtil {

    // jwt令牌
    private static final String AUTH_TOKEN = "X-AUTH-TOKEN";
    // 用户信息，用于前端展示
    private static final String LOGIN_USER = "X-LOGIN-USER";
    // 过期时间5分钟
    public static long EXPIRE_TIME = 5 * 60 * 1000L;
    // refreshToken过期时间
    public static long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;

    public static String accessToken(HttpServletResponse response,
                                     TokenUser tokenUser, Long expiresMillis) {
        if (tokenUser == null) {
            return null;
        }
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = EXPIRE_TIME;
        }
        String jsonUser = JSON.toJSONString(tokenUser);
        JWTUtil jwtUtil = new JWTUtil();
        String authToken = jwtUtil.sign(jsonUser, expiresMillis);
        response.setHeader(LOGIN_USER, jsonUser);
        return authToken;
    }
    
    public static String refreshToken(Long expiresMillis) {
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = EXPIRE_TIME;
        }
        JWTUtil jwtUtil = new JWTUtil();
        return jwtUtil.sign(null, expiresMillis);
    }

    public static TokenUser getTokenUser(HttpServletRequest request) {
        String authToken = request.getHeader(AUTH_TOKEN);
        if (StringUtils.isBlank(authToken)) {
            return null;
        }
        JWTUtil jwtUtil = new JWTUtil();
        String jsonUser = jwtUtil.verify(authToken);
        if (StringUtils.isBlank(jsonUser)) {
            return null;
        }
        return JSON.parseObject(jsonUser, TokenUser.class);
    }

    //伪删除,客户端仍然可以提交未过期token
    public static boolean deleteTokenUser(HttpServletRequest request, HttpServletResponse response) {
        String loginUser = request.getHeader(LOGIN_USER);
        if (StringUtils.isBlank(loginUser)) {
            return true;
        }
        response.setHeader(LOGIN_USER, null);
        return true;
    }
}
