package com.mss.framework.base.user.server.web.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: cookie工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 13:56
 */
@Configuration
@Slf4j
public class CookieUtil {

    @Value("${cookie.name}")
    private static final String COOKIE_NAME = "login_token";
    @Value("${cookie.domain}")
    private static final String COOKIE_DOMAIN = "localhost";

    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //单位是秒
        //maxAge不设置的话，cookie不会写入硬盘，而是写在内存，只在当前页面有效
        cookie.setMaxAge(60*60*24*365);//如果是-1代表永久
        log.info("write cookieName:{} cookieValue:{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                log.info("read cookieName:{} cookieValue:{}", cookie.getName(), cookie.getValue());
                if (COOKIE_NAME.equals(cookie.getName())){
                    log.info("return cookieName:{} cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void deleteLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (COOKIE_NAME.equals(cookie.getName())){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);//设置成0，代表删除此cookie
                    log.info("delete cookieName:{} cookieValue:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
