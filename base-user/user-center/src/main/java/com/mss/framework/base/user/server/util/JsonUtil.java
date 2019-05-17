package com.mss.framework.base.user.server.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class JsonUtil {

    private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 序列化
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为标准格式
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));

        // 反序列化
        //忽略在json字符串中存在，但是在java对象中不存的对应属性，防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String objToStr(T obj){
        if (obj == null){
            return null;
        }
        try{
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e){
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static <T> String objToStrPretty(T obj){
        if (obj == null){
            return null;
        }
        try{
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e){
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static <T> T strToObj(String str, Class<T> clazz){
        if (StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str : objectMapper.readValue(str, clazz);
        } catch (Exception e){
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    public static <T> T strToObj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e){
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    public static <T> T strToObj(String str, Class<?> collectionClass, Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e){
            log.warn("Parse String to Object error", e);
            return null;
        }
    }
}
