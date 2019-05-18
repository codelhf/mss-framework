package com.mss.framework.base.user.server.web.token;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 11:49
 */
@Configuration
@Slf4j
public class TokenUtil {

    // jwt令牌
    private static final String AUTH_TOKEN = "X-AUTH-TOKEN";
    // 用户信息，用于前端展示
    private static final String LOGIN_USER = "X-LOGIN-USER";
    // 过期时间5分钟
    private static long EXPIRE_TIME = 5 * 60 * 1000;

    public static boolean putTokenUser(HttpServletResponse response,
                                       TokenUser tokenUser, Long expiresMillis) {
        if (tokenUser == null) {
            return false;
        }
        if (expiresMillis != null && expiresMillis > 0) {
            EXPIRE_TIME = expiresMillis;
        }
        String jsonUser = JSON.toJSONString(tokenUser);
        String authToken = JWTUtil.sign(jsonUser, EXPIRE_TIME);
        response.setHeader(AUTH_TOKEN, authToken);
        response.setHeader(LOGIN_USER, jsonUser);
        return true;
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

    //伪删除,客户端仍然可以提交未过期token
    public static boolean deleteTokenUser(HttpServletRequest request, HttpServletResponse response) {
        String authToken = request.getHeader(AUTH_TOKEN);
        if (StringUtils.isBlank(authToken)) {
            return true;
        }
        response.setHeader(AUTH_TOKEN, null);
        response.setHeader(LOGIN_USER, null);
        return true;
    }
}
