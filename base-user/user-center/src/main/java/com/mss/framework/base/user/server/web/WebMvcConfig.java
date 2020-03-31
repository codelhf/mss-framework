package com.mss.framework.base.user.server.web;

import com.mss.framework.base.user.server.web.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Description: Webmvc相关配置
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:48
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // oauth2.0认证服务
        registry.addInterceptor(oAuthInterceptor())
                .addPathPatterns("/oauth2.0/authorize");
        registry.addInterceptor(oAuthAccessTokenInterceptor())
                .addPathPatterns("/oauth2.0/verify");
        // 单点登录只在浏览器场景使用
        registry.addInterceptor(new SSOInterceptor())
                .addPathPatterns("/sso/token");
        registry.addInterceptor(new SSOAccessTokenInterceptor())
                .addPathPatterns("/sso/verify");
        // 用户服务拦截请求
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                //排除用户部分接口, refresh_token
                .excludePathPatterns("/user/v1.0/**", "/sso/refresh_token", "/oauth2.0/refresh_token");
    }

    /**
     * 接口拦截器中mapper注入为空的问题
     * 因为拦截器的加载在springcontext之前，所以自动注入的mapper是null
     * @return
     */
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public OAuthInterceptor oAuthInterceptor() {
        return new OAuthInterceptor();
    }

    @Bean
    public OAuthAccessTokenInterceptor oAuthAccessTokenInterceptor() {
        return new OAuthAccessTokenInterceptor();
    }

    @Bean
    public SSOInterceptor ssoInterceptor() {
        return new SSOInterceptor();
    }

    @Bean
    public SSOAccessTokenInterceptor ssoAccessTokenInterceptor() {
        return new SSOAccessTokenInterceptor();
    }

}
