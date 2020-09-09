package com.onoff.wechatofficialaccount.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/16 18:56
 * @VERSION 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 配置静态访问资源
     *
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // classpath表示在resource目录下，/static/** 表示在URL路径中访问如
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * 拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/*").excludePathPatterns("/onoff_login.html","/past/","/sign_in", "/wechat/check","/middle","/list.html/","/record/","/static/**","/MP_verify_5z40QMC3xdtOZ81T.txt");
    }

}
