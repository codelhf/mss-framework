package com.mss.framework.base.user.server.web.token;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.util.ResponseUtil;
import com.mss.framework.base.user.server.web.token.cookie.CookieUtil;
import com.mss.framework.base.user.server.web.token.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static String accessToken(User user, Long expiresMillis) {
        if (user == null) {
            return null;
        }
        TokenUser tokenUser = getTokenUser(user);
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = JWTUtil.EXPIRE_TIME;
        }
        return JWTUtil.sign(JSON.toJSONString(tokenUser), JWTUtil.secret, JWTUtil.issuer, expiresMillis);
    }
    
    public static String refreshToken(String userId, Long expiresMillis) {
        if (expiresMillis == null || expiresMillis < 0) {
            expiresMillis = JWTUtil.REFRESH_TOKEN_EXPIRE_TIME;
        }
        return JWTUtil.sign(userId, JWTUtil.secret, JWTUtil.issuer, expiresMillis);
    }

    public static TokenUser getTokenUser(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        String jsonUser = JWTUtil.verify(accessToken, JWTUtil.secret, JWTUtil.issuer);
        if (StringUtils.isBlank(jsonUser)) {
            return null;
        }
        return JSON.parseObject(jsonUser, TokenUser.class);
    }

    public static String getUserId(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return null;
        }
        return JWTUtil.verify(refreshToken, JWTUtil.secret, JWTUtil.issuer);
    }

    public static String sign(String str, Long expiresIn, TimeUnit timeUnit) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (expiresIn == null || expiresIn < 0) {
            expiresIn = JWTUtil.EXPIRE_TIME;
        } else {
            expiresIn = timeUnit.toMillis(expiresIn);
        }
        return JWTUtil.sign(str, JWTUtil.secret, JWTUtil.issuer, expiresIn);
    }

    public static String verify(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return JWTUtil.verify(token, JWTUtil.secret, JWTUtil.issuer);
    }

    public static Map<String, Object> putSSOToken(User user, List<String> cookieDomains, HttpServletResponse response) {
        // 生成token
        String accessToken = accessToken(user, JWTUtil.EXPIRE_TIME);
        String refreshToken = refreshToken(user.getId(), JWTUtil.REFRESH_TOKEN_EXPIRE_TIME);
        // 将token写入所有域名下, 针对sso
        for (String cookieDomain : cookieDomains) {
            cookieDomain = getCookieDomain(cookieDomain);
            CookieUtil.writeToken(response, accessToken, CookieUtil.ACCESS_TOKEN, cookieDomain, CookieUtil.EXPIRE_TIME);
            CookieUtil.writeToken(response, refreshToken, CookieUtil.REFRESH_TOKEN, cookieDomain, CookieUtil.MAX_AGE);
        }
        return ResponseUtil.SSOData(accessToken, refreshToken, JWTUtil.EXPIRE_TIME, JSON.toJSONString(user));
    }

    public static String readAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(JWTUtil.ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            //是否为浏览器登录
            accessToken = CookieUtil.readToken(request, CookieUtil.ACCESS_TOKEN);
        }
        return accessToken;
    }

    public static boolean deleteSSOToken(String accessToken, List<String> cookieDomains, HttpServletResponse response) {
        // TODO 将token放入黑名单中

        // 删除所有域名下的token
        for (String cookieDomain : cookieDomains) {
            cookieDomain = getCookieDomain(cookieDomain);
            CookieUtil.writeToken(response, accessToken, CookieUtil.ACCESS_TOKEN, cookieDomain, 0);
//            CookieUtil.writeToken(response, refreshToken, CookieUtil.REFRESH_TOKEN, cookieDomain, 0);
        }
        return true;
    }

    private static TokenUser getTokenUser(User user) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(user.getId());
        tokenUser.setUsername(user.getNickname());
        tokenUser.setEmail(user.getEmail());
        tokenUser.setPhone(user.getPhone());
        return tokenUser;
    }

    private static String getCookieDomain(String fullDomain) {
        // www.abc.aab.domain.com.cn
        // www.domain.xxx
        // login.domain.xxx
        if (StringUtils.isBlank(fullDomain)) {
            return null;
        }
        //只允许一级子域名
        int begin = fullDomain.indexOf(".");
        int end = fullDomain.indexOf("/");
        return fullDomain.substring(begin, end);
    }
}
