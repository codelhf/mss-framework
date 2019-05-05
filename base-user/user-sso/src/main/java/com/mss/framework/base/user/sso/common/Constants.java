package com.mss.framework.base.user.sso.common;

/**
 * @Description: 公共常量类
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 23:10
 */
public class Constants {

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * Access Token在session中存储的变量名
     */
    public static final String SESSION_ACCESS_TOKEN = "ACCESS_TOKEN";

    /**
     * 用户信息在session中存储的变量名
     */
    public static final String SESSION_USER = "SESSION_USER";

    /**
     * Access Token在session中存储的变量名
     */
    public static final String COOKIE_ACCESS_TOKEN = "ACCESS_TOKEN";

    /**
     * Refresh Token在session中存储的变量名
     */
    public static final String COOKIE_REFRESH_TOKEN = "REFRESH_TOKEN";
}
