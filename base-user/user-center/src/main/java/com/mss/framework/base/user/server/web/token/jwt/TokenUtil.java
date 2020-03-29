package com.mss.framework.base.user.server.web.token.jwt;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.util.ResponseUtil;
import com.mss.framework.base.user.server.web.session.sso.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private static final long EXPIRE_TIME = 5 * 60 * 1000L;
    // refreshToken过期时间
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;
    // 支持的域名
    private static final List<String> cookieDomains = Arrays.asList(".taobao.com", ".tmall.com");

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
    
    public static String refreshToken(String userId, Long expiresMillis) {
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = REFRESH_TOKEN_EXPIRE_TIME;
        }
        return JWTUtil.sign(userId, secret, issuer, expiresMillis);
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

    public static String getUserId(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return null;
        }
        return JWTUtil.verify(refreshToken, secret, issuer);
    }

    public static Map<String, Object> putSSOToken(User user, HttpServletResponse response) {
        TokenUser tokenUser = tokenUser(user);
        // 生成token
        String accessToken = TokenUtil.accessToken(tokenUser, TokenUtil.EXPIRE_TIME);
        String refreshToken = TokenUtil.refreshToken(tokenUser.getId(), TokenUtil.REFRESH_TOKEN_EXPIRE_TIME);
        // 将token写入所有域名下, 针对sso
        for (String cookieDomain : cookieDomains) {
            CookieUtil.writeToken(response, accessToken, CookieUtil.ACCESS_TOKEN, cookieDomain, CookieUtil.EXPIRE_TIME);
            CookieUtil.writeToken(response, refreshToken, CookieUtil.REFRESH_TOKEN, cookieDomain, CookieUtil.MAX_AGE);
        }
        return ResponseUtil.SSOData(accessToken, refreshToken, TokenUtil.EXPIRE_TIME, JSON.toJSONString(user));
    }

    public static String readAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTH_TOKEN);
        if (accessToken == null) {
            //是否为浏览器登录
            accessToken = CookieUtil.readToken(request, CookieUtil.ACCESS_TOKEN);
        }
        return accessToken;
    }

    public static boolean deleteSSOToken(String accessToken, HttpServletResponse response) {
        // TODO 将token放入黑名单中

        // 删除所有域名下的token
        for (String cookieDomain : cookieDomains) {
            CookieUtil.writeToken(response, accessToken, CookieUtil.ACCESS_TOKEN, cookieDomain, 0);
//            CookieUtil.writeToken(response, refreshToken, CookieUtil.REFRESH_TOKEN, cookieDomain, 0);
        }
        return true;
    }

    public static TokenUser tokenUser(User user) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(user.getId());
        tokenUser.setUsername(user.getNickname());
        tokenUser.setEmail(user.getEmail());
        tokenUser.setPhone(user.getPhone());
        return tokenUser;
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
