package com.sian.translate.config;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class MyBeanSerializerModifier extends BeanSerializerModifier {

    private JsonSerializer<Object> _nullArrayJsonSerializer = new MyNullArrayJsonSerializer();

    private JsonSerializer<Object> _nullStringJsonSerializer = new MyNullStringJsonSerializer();

    private JsonSerializer<Object> _nullIntegerJsonSerializer = new MyNullIntegerJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            // 判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                //给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
            }
            if (isStringType(writer)) {
                writer.assignNullSerializer(this.defaultNullStringJsonSerializer());
            }

            if (isDateType(writer)) {
                writer.assignNullSerializer(this.defaultNullStringJsonSerializer());
            }
            if (isIntegerType(writer)) {
                writer.assignNullSerializer(this.defaultNullIntegerJsonSerializer());
            }
        }
        return beanProperties;
    }

    // 判断是什么类型
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
    }

    protected boolean isStringType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.equals(String.class);
    }

    protected boolean isDateType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.equals(Date.class);
    }

    protected boolean isIntegerType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.equals(Integer.class) || clazz.equals(int.class);
    }

    protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
        return _nullArrayJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullStringJsonSerializer() {
        return _nullStringJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullIntegerJsonSerializer() {
        return _nullIntegerJsonSerializer;
    }
}