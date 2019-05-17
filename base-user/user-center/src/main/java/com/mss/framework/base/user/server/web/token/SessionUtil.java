package com.mss.framework.base.user.server.web.token;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.server.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: session工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 13:56
 */
@Configuration
@Slf4j
public class SessionUtil {

    @Autowired
    private static RedisUtil redisUtil;

    @Value("${token.jwt.secret}")
    private static String secret;
    @Value("${token.jwt.issuer}")
    private static String issuer;

    // 过期时间5分钟
    private static long EXPIRE_TIME = 5 * 60 * 1000;

    public static boolean putTokenUser(HttpServletRequest request, HttpServletResponse response,
                                    TokenUser tokenUser, Long expiresMillis) {
        if (expiresMillis != null && expiresMillis > 0) {
            EXPIRE_TIME = expiresMillis;
        }
        String loginToken = request.getSession().getId();
        String jsonUser = JSON.toJSONString(tokenUser);
        CookieUtil.writeLoginToken(response, loginToken);
        return redisUtil.setExpire(loginToken, jsonUser, EXPIRE_TIME);
    }

    public static TokenUser getTokenUser(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)) {
            return null;
        }
        String jsonUser = (String) redisUtil.get(loginToken);
        if (StringUtils.isBlank(jsonUser)) {
            return null;
        }
        return JSON.parseObject(jsonUser, TokenUser.class);
    }

    public static boolean deleteTokenUser(HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)) {
            return true;
        }
        String jsonUser = (String) redisUtil.get(loginToken);
        if (StringUtils.isBlank(jsonUser)) {
            return true;
        }
        CookieUtil.deleteLoginToken(request, response);
        redisUtil.del(loginToken);
        return true;
    }
}
