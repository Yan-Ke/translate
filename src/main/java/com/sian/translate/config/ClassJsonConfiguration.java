package com.sian.translate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ClassJsonConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = converter.getObjectMapper();

        // 为mapper注册一个带有SerializerModifier的Factory，此modifier主要做的事情为：判断序列化类型，根据类型指定为null时的值

        mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));

        return converter;
    }

}