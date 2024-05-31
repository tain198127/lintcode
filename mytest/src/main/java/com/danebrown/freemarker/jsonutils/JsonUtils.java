package com.danebrown.freemarker.jsonutils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @description: json 工具类
 * @author: hanxu-jr
 * @date: 2020/12/15 11:26
 */
public class JsonUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static ObjectMapper mapper;
    
    static {
        mapper = mapperSetting(new ObjectMapper());
        
    }
    
    public static ObjectMapper mapperSetting(ObjectMapper mapper) {
        //支持LocalDateTime序列化
        SimpleModule module = new SimpleModule("LocalDateTime Serialiser", new Version(0, 1, 1, "FINAL", null, null));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
        module.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
        module.addSerializer(Duration.class, DurationSerializer.INSTANCE);
        
        mapper.registerModule(module);
        
        //关闭格式化输出
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
        //只转化非空字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //空值不报错
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //枚举字段支持
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        //禁止枚举字段传数字
        mapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        //忽略空字段
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 反序列化支持bigDecimal
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        // 不输出科学计数法
        mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        return mapper;
    }
    
    /**
     * 将一个对象序列化成String
     *
     * @param object 序列化对象
     * @return 序列化String
     * @throws IOException
     */
    public static String serializeToString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
    
    /**
     * 将一个对象序列化成String
     *
     * @param object 序列化对象
     * @return 序列化String
     * @throws IOException
     */
    public static String serializeToString(ObjectMapper mapper, Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
    
    /**
     * 将一个对象序列化成byte
     *
     * @param object 序列化对象
     * @return 序列化byte
     * @throws IOException
     */
    public static byte[] serializeToBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }
    
    /**
     * 将一个对象序列化成byte
     *
     * @param object 序列化对象
     * @return 序列化byte
     * @throws IOException
     */
    public static byte[] serializeToBytes(ObjectMapper mapper, Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }
    
    /**
     * 反序列化
     *
     * @param jsonString json字符串
     * @param cls 转化目标类型
     * @param <T> 转化目标
     * @return 转化后的bean
     * @throws IOException
     */
    public static <T> T deserialize(String jsonString, Class<T> cls) throws IOException {
        return mapper.readValue(jsonString, cls);
    }
    
    /**
     * 反序列化
     *
     * @param jsonString json字符串
     * @param cls 转化目标类型
     * @param <T> 转化目标
     * @return 转化后的bean
     * @throws IOException
     */
    public static <T> T deserialize(ObjectMapper mapper, String jsonString, Class<T> cls) throws IOException {
        return mapper.readValue(jsonString, cls);
    }
    
    /**
     * 反序列化
     *
     * @param jsonString json字符串
     * @param type 转化目标类型
     * @param <T> 转化目标
     * @return 转化后的bean
     * @throws IOException
     */
    public static <T> T deserialize(String jsonString, Type type) throws IOException {
        return mapper.readValue(jsonString, mapper.constructType(type));
    }
    
    /**
     * 反序列化
     *
     * @param jsonString json字符串
     * @param type 转化目标类型
     * @param <T> 转化目标
     * @return 转化后的bean
     * @throws IOException
     */
    public static <T> T deserialize(ObjectMapper mapper, String jsonString, Type type) throws IOException {
        return mapper.readValue(jsonString, mapper.constructType(type));
    }
    
    /**
     * 类型转化
     *
     * @param fromValue 原始数据
     * @param toValueType 转化目标类型
     * @param <T> 转化目标类型
     * @return 转化后的bean
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }
    
    /**
     * 类型转化
     *
     * @param fromValue 原始数据
     * @param toValueType 转化目标类型
     * @param <T> 转化目标类型
     * @return 转化后的bean
     */
    public static <T> T convertValue(ObjectMapper mapper, Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }
    
    public static ObjectMapper getObjectMapper() {
        return mapper;
    }
}
