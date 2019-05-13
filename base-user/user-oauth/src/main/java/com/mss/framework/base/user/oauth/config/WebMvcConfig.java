package com.mss.framework.base.user.oauth.config;

import com.mss.framework.base.user.oauth.interceptor.AuthInterceptor;
import com.mss.framework.base.user.oauth.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: Web相关配置
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:53
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/**");
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/login");
    }
}
