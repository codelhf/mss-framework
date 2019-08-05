package com.mss.framework.base.user.server.web.session.sso;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: cookie工具类
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 13:56
 */
@Slf4j
public class CookieUtil {

    public static void writeLoginToken(HttpServletResponse response, String token, String cookieName,
                                       String cookieDomain, int maxAge){
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setDomain(cookieDomain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //单位是秒
        //maxAge不设置的话，cookie不会写入硬盘，而是写在内存，只在当前页面有效
        cookie.setMaxAge(maxAge);//如果是-1代表永久
        log.info("write cookieName:{} cookieValue:{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                log.info("read cookieName:{} cookieValue:{}", cookie.getName(), cookie.getValue());
                if (cookieName.equals(cookie.getName())){
                    log.info("return cookieName:{} cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void deleteLoginToken(HttpServletRequest request, HttpServletResponse response, String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookieName.equals(cookie.getName())){
                    cookie.setDomain(cookieName);
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
