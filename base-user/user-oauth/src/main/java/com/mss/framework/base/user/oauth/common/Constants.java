package com.mss.framework.base.user.oauth.common;

/**
 * @Description: 公共常量类
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:24
 */
public class Constants {
    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 请求Authorization Code的随机状态码在session中存储的变量名
     */
    public static final String SESSION_AUTH_CODE_STATUS = "AUTH_CODE_STATUS";

    /**
     * Access Token在session中存储的变量名
     */
    public static final String SESSION_ACCESS_TOKEN = "ACCESS_TOKEN";

    /**
     * 用户信息在session中存储的变量名
     */
    public static final String SESSION_USER = "SESSION_USER";
}
