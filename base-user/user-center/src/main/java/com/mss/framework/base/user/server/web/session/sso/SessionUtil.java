package com.mss.framework.base.user.server.web.session.sso;

import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.user.server.web.token.jwt.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: session工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 13:56
 */
@Slf4j
public class SessionUtil {

//    private static RedisUtil redisUtil;

    private static final String COOKIE_NAME = "login_token";
    // access token过期时间5分钟
    public static final int EXPIRE_TIME = 60*5;
    // refresh token过期时间
    public static final int MAX_AGE = 60*60*24*365;

    public static boolean putTokenUser(HttpServletResponse response, TokenUser tokenUser, Integer expiresMillis, List<String> cookieDomains) {
        if (tokenUser == null) {
            return false;
        }
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = EXPIRE_TIME;
        }
        String loginToken = TokenUtil.accessToken(tokenUser, expiresMillis.longValue());
        //将token写入所有域名下
        for (String cookieDomain: cookieDomains) {
            CookieUtil.writeLoginToken(response, loginToken, COOKIE_NAME, cookieDomain, expiresMillis);
        }
        return true;
    }

    public static TokenUser getTokenUser(HttpServletRequest request) {
//        if (redisUtil == null) {
//            redisUtil = SpringContextUtil.getBeanByName("redisUtil", RedisUtil.class);
//        }
        String loginToken = CookieUtil.readLoginToken(request, COOKIE_NAME);
        //未登录
        if (StringUtils.isBlank(loginToken)) {
            return null;
        }
        //验证token未过期，但是用户已登出的黑名单中
//        String tokenStr = (String) redisUtil.get(loginToken);
//        if (StringUtils.isNotBlank(tokenStr)) {
//            return null;
//        }
        return TokenUtil.getTokenUser(request);
    }

    public static boolean deleteTokenUser(HttpServletRequest request, HttpServletResponse response) {
//        if (redisUtil == null) {
//            redisUtil = SpringContextUtil.getBeanByName("redisUtil", RedisUtil.class);
//        }
        String loginToken = CookieUtil.readLoginToken(request, COOKIE_NAME);
        if (StringUtils.isBlank(loginToken)) {
            return true;
        }
        CookieUtil.deleteLoginToken(request, response, COOKIE_NAME);
//        String tokenStr = (String) redisUtil.get(loginToken);
//        if (StringUtils.isBlank(tokenStr)) {
//            redisUtil.setExpire(tokenStr, tokenStr, EXPIRE_TIME);
//        }
        return true;
    }
}
