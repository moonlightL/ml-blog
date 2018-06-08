package com.extlight.common.utils;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import java.text.SimpleDateFormat;

public abstract class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        // 取消默认转换 timestamps 形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略空 Bean 转 json 的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        // 忽略在 json 字符串中存在，在 java 对象不存在的属性，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 统一日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串转对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T string2Obj(String json, Class<T> clazz) {
        if (json == null || "".equals(json) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T string2Obj(String json, TypeReference<T> typeReference) {
        if (json == null || "".equals(json) || typeReference == null) {
            return null;
        }

        try {
            return typeReference.getType().equals(String.class) ? (T) json : objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T string2Obj(String json, Class<?> collectionClass, Class<?> ... elementClasses) {
        if (json == null || "".equals(json) || collectionClass == null) {
            return null;
        }

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

