package com.mss.framework.base.user.server.web;

import com.mss.framework.base.user.server.web.interceptor.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: Webmvc相关配置
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:48
 */
@Configuration
//@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("*/user/v1.0/**")//排除用户部分接口
                .addPathPatterns("*/**");
        registry.addInterceptor(new SSOInterceptor())
                .addPathPatterns("*/sso/token");
        registry.addInterceptor(new SSOAccessTokenInterceptor())
                .addPathPatterns("*/**");
        registry.addInterceptor(new OauthInterceptor())
                .addPathPatterns("*/oauth2.0/authorize");
        registry.addInterceptor(new OAuthAccessTokenInterceptor())
                .addPathPatterns("*/oauth2.0/**");
    }

}
