package com.mss.framework.base.user.server.web;

import com.mss.framework.base.core.token.TokenUser;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {

    private static final ThreadLocal<TokenUser> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void add(TokenUser user){
        userHolder.set(user);
    }

    public static void add(HttpServletRequest request){
        requestHolder.set(request);
    }

    public static TokenUser getCurrentUser(){
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest(){
        return requestHolder.get();
    }

    public static void remove(){
        userHolder.remove();
        requestHolder.remove();
    }

}
