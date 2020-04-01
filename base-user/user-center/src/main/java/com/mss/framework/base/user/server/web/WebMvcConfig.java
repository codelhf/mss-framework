package com.mss.framework.base.user.server.web;

import com.mss.framework.base.user.server.web.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: Webmvc相关配置
 * WebMvcConfigurationSupport 在整个应用程序中只会生效一个
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:48
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private OAuthInterceptor oAuthInterceptor;
    @Autowired
    private OAuthAccessTokenInterceptor oAuthAccessTokenInterceptor;
    @Autowired
    private SSOInterceptor ssoInterceptor;
    @Autowired
    private SSOAccessTokenInterceptor ssoAccessTokenInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户服务拦截请求
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                //排除用户部分接口, refresh_token
                .excludePathPatterns("/user/v1.0/**", "/sso/refresh_token", "/oauth2.0/refresh_token");
        // oauth2.0认证服务
        registry.addInterceptor(oAuthInterceptor)
                .addPathPatterns("/oauth2.0/authorize");
        registry.addInterceptor(oAuthAccessTokenInterceptor)
                .addPathPatterns("/oauth2.0/verify");
        // 单点登录只在浏览器场景使用
        registry.addInterceptor(ssoInterceptor)
                .addPathPatterns("/sso/token");
        registry.addInterceptor(ssoAccessTokenInterceptor)
                .addPathPatterns("/sso/verify");
    }

}
