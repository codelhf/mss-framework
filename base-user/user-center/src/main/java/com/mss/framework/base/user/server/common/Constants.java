package com.mss.framework.base.user.server.common;

/**
 * @Description: 常量类
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 16:39
 */
public class Constants {

    /**
     * 用户信息在session中存储的变量名
     */
    public static final String SESSION_USER = "SESSION_USER";

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 授权页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";

    public interface LoginType {
        String USERNAME = "username";
        String USERNAME_PARTTERN = "^\\s\\S";
        String PHONE = "phone";
        String PHONE_PARTTERN = "^1\\d+(0-9)$";
        String EMAIL = "email";
        String EMAIL_PARTTERN = "";
    }
}
