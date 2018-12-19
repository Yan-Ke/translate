package com.sian.translate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Value("${file.rootPath}")
    //图片存放根路径
    private String rootPath ;

    @Value("${file.sonPath}")
    //图片存放根目录下的子目录
    private String sonPath ;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //指向外部目录
        registry.addResourceHandler(sonPath+"/**").addResourceLocations("file:" + rootPath + sonPath);
        super.addResourceHandlers(registry);
    }
}
