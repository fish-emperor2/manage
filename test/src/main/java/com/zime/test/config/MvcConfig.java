package com.zime.test.config;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //String path = System.getProperty("user.dir")+"\\upload\\";
        //registry.addResourceHandler("/upload/**").addResourceLocations("file:"+path);
        registry.addResourceHandler("/upload/**").addResourceLocations("file:upload/");
    }
}
