package com.gibbsdevops.alfred.model.alfred.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.FactoryBean;

public class AlfredObjectMapperFactory implements FactoryBean<ObjectMapper> {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public static ObjectMapper get() {
        return objectMapper;
    }

    @Override
    public ObjectMapper getObject() {
        return objectMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
