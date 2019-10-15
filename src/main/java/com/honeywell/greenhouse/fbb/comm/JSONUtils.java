package com.honeywell.greenhouse.fbb.comm;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils implements ApplicationContextAware {

    private static volatile ObjectMapper defaultObjectMapper = MiscUtils.createDefaultObjectMapper();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        if (objectMapper != null) {
            defaultObjectMapper = objectMapper;
        }
    }

    /**
     * @param javaObject the java object to be converted.
     * @return the JSON string on behalf of given object.
     */
    public static String toJSONString(@Nullable Object javaObject) {
        try {
            return defaultObjectMapper.writeValueAsString(javaObject);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * @param javaObject  the java object to be converted.
     * @param forcePretty force print pretty json or not; if false fall back to {@link #toJSONString(Object)}
     * @return the JSON string on behalf of given object.
     */
    public static String toJSONString(@Nullable Object javaObject, boolean forcePretty) {
        if (forcePretty) {
            try {
                return defaultObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(javaObject);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        } else {
            return toJSONString(javaObject);
        }
    }

    /**
     * @param jsonString
     * @param valueType
     * @return
     */
    public static <T> T fromJSONString(@Nonnull String jsonString, @Nonnull Class<T> valueType) {
        try {
            return defaultObjectMapper.readValue(jsonString, valueType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * @param jsonString
     * @param valueTypeRef
     * @return
     */
    public static <T> T fromJSONString(@Nonnull String jsonString, @Nonnull TypeReference<T> valueTypeRef) {
        try {
            return defaultObjectMapper.readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * @param jsonString
     * @param parametrized
     * @param parameterClasses
     * @return
     */
    public static <T> T fromJSONString(@Nonnull String jsonString, @Nonnull Class<?> parametrized, @Nonnull Class<?> parameterClasses) {
        try {
            JavaType valueType = defaultObjectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
            return defaultObjectMapper.readValue(jsonString, valueType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * @param jsonString
     * @param parametrized
     * @param parameterClasses
     * @return
     */
    public static <T> T fromJSONStringToList(@Nonnull String jsonString, @Nonnull Class<?> parametrized, @Nonnull Class<?> parameterClasses) {
        try {
            JavaType parameterType = defaultObjectMapper.getTypeFactory().constructCollectionType(List.class, parameterClasses);
            parameterType = defaultObjectMapper.getTypeFactory().constructParametricType(parametrized, parameterType);
            return defaultObjectMapper.readValue(jsonString, parameterType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T convertValue(@Nullable Object fromValue, @Nonnull Class<T> toValueType) {
        return defaultObjectMapper.convertValue(fromValue, toValueType);
    }


}
