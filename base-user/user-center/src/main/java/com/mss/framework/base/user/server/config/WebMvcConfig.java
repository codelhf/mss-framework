package com.mss.framework.base.user.server.config;

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
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/**","/oauth2.0/authorizePage","/oauth2.0/authorize","/sso/token");
//        registry.addInterceptor(oauthInterceptor()).addPathPatterns("/oauth2.0/authorize");
//        registry.addInterceptor(accessTokenInterceptor()).addPathPatterns("/api/**");
//        registry.addInterceptor(ssoAccessDomainInterceptor()).addPathPatterns("/sso/token");
//        registry.addInterceptor(ssoAccessTokenInterceptor()).addPathPatterns("/sso/verify");
    }

//    @Bean
//    public OauthInterceptor oauthInterceptor(){
//        return new OauthInterceptor();
//    }
//
//    @Bean
//    public OAuthAccessTokenInterceptor accessTokenInterceptor(){
//        return new OAuthAccessTokenInterceptor();
//    }
//
//    @Bean
//    public SsoAccessTokenInterceptor ssoAccessTokenInterceptor(){
//        return new SsoAccessTokenInterceptor();
//    }
//
//    @Bean
//    public SsoAccessDomainInterceptor ssoAccessDomainInterceptor(){
//        return  new SsoAccessDomainInterceptor();
//    }

}
