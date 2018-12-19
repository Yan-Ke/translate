package com.sian.translate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class LoadConfig {

    /**当前端口**/
    private String port;

    /**图片存放根路径**/
    private String rootPath;

    /**图片存放根目录下的子目录**/
    private String sonPath;

}
