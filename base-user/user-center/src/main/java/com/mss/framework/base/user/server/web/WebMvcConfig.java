package com.mss.framework.base.user.server.web;

import com.mss.framework.base.user.server.web.interceptor.LoginInterceptor;
import com.mss.framework.base.user.server.web.interceptor.OauthInterceptor;
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
        registry.addInterceptor(new OauthInterceptor())
                .addPathPatterns("/oauth2.0/authorize");
//        registry.addInterceptor(accessTokenInterceptor()).addPathPatterns("/api/**");
//        registry.addInterceptor(ssoAccessDomainInterceptor()).addPathPatterns("/sso/token");
//        registry.addInterceptor(ssoAccessTokenInterceptor()).addPathPatterns("/sso/verify");
    }

    @Bean
    @Order(1)
    public FilterRegistrationBean CharacterEncodingFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CharacterEncodingFilter("utf-8", true));
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean//order的数值越小 则优先级越高
    @Order(2)//HttpPutFormContentFilter接口Put请求无法获取请求体的内容
    public FilterRegistrationBean HttpPutFormContentFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new HttpPutFormContentFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
