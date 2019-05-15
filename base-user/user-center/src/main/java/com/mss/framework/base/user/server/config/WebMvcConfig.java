package com.mss.framework.base.user.server.config;

import com.mss.framework.base.user.server.web.interceptor.LoginInterceptor;
import com.mss.framework.base.user.server.web.interceptor.OauthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: Web相关配置
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:48
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/**","/oauth2.0/authorizePage","/oauth2.0/authorize","/sso/token");
        registry.addInterceptor(new OauthInterceptor()).addPathPatterns("/oauth2.0/authorize");
//        registry.addInterceptor(accessTokenInterceptor()).addPathPatterns("/api/**");
//        registry.addInterceptor(ssoAccessDomainInterceptor()).addPathPatterns("/sso/token");
//        registry.addInterceptor(ssoAccessTokenInterceptor()).addPathPatterns("/sso/verify");
    }

}
