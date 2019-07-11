package com.mss.framework.base.core.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: Webmvc相关配置
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:48
 */
@Configuration
//@EnableWebMvc
public class WebMvcFilter {

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
