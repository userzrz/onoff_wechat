package com.onoff.wechatofficialaccount.config;

import org.springframework.context.annotation.Configuration;
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
    /**
     * 配置静态访问资源
     *
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // classpath表示在resource目录下，/static/** 表示在URL路径中访问如
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

}
