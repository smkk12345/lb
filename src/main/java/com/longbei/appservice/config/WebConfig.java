package com.longbei.appservice.config;


import com.longbei.appservice.common.security.SecurityFilter;
import com.longbei.appservice.common.security.SpringUtil;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 * Created by longbei on 2016/9/8.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(createUserLoginInterceptor()).addPathPatterns("/business/in/**");
//        registry.addInterceptor(createTokenInterceptor()).addPathPatterns("/business/api/**");
//        super.addInterceptors(registry);
    }

//    @Bean
//    public FilterRegistrationBean indexFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean(new SecurityFilter());
//        registration.addUrlPatterns("/");
//        return registration;
//    }
    
    @Bean
    public SpringUtil initSpringUtil(){
    	return new SpringUtil();
    }



}
