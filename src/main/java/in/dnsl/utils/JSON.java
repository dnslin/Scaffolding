package in.dnsl.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

/**
 * JSON 工具类
 */
@NoArgsConstructor
public abstract class JSON {


    private static final ObjectMapper mapper;
    private static final ObjectMapper withEmptyMapper;


    @Getter
    private static final SimpleModule dateModule;
    private static final Logger logger = LoggerFactory.getLogger(JSON.class);

    static {
        //datetime parse
        dateModule = new SimpleModule();
        //配置序列化
        dateModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dateModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dateModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        // 这里还要配置反序列化, 暂时跳过
//        dateModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        //配置反序列化
        dateModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dateModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dateModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        //without empty
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        buildCommonMapper(mapper);
        //within empty
        withEmptyMapper = new ObjectMapper();
        withEmptyMapper.setSerializationInclusion(Include.ALWAYS);
        buildCommonMapper(withEmptyMapper);
    }

    /**
     * 设置 mapper 的通用属性
     */
    private static void buildCommonMapper(ObjectMapper mapper) {
        mapper.registerModule(dateModule);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象转换成 json
     *
     * @param originalObject 要转换的对象
     */
    public static String toJSON(Object originalObject) {

        if (originalObject == null) return null;
        if (originalObject instanceof String) return String.valueOf(originalObject);

        String json = null;
        try {
            json = mapper.writeValueAsString(originalObject);
        } catch (Exception e) {
            logger.error("toJson error:", e);
        }

        return json;
    }

    /**
     * 将对象转换成 json
     *
     * @param originalObject 要转换的对象
     */
    public static byte[] toJsonByte(Object originalObject) {

        if (originalObject == null) return null;

        byte[] json = null;
        try {
            json = mapper.writeValueAsBytes(originalObject);
        } catch (Exception e) {
            logger.error("toJson error:", e);
        }

        return json;
    }


    /**
     * 将对象转换成 json,并包含空属性
     *
     * @param originalObject 要转换的对象
     */
    public static String toJsonWithEmpty(Object originalObject) {

        if (originalObject == null) return null;
        String json = null;
        try {
            json = withEmptyMapper.writeValueAsString(originalObject);
        } catch (Exception e) {
            logger.error("toJson error:", e);
        }

        return json;
    }

    /**
     * 进行类型转换
     */
    public static <T> T convert(Object origin, Class<T> targetClass) {
        return mapper.convertValue(origin, targetClass);
    }

    /**
     * 将 list 转为另一个 list
     *
     * @param originList  原始 List
     * @param targetClass 目标类型
     * @return 目标类型 List
     */
    public static <O extends Collection<?>, T> List<T> convertList(O originList, Class<T> targetClass) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(originList.getClass(), targetClass);
        return mapper.convertValue(originList, javaType);
    }


    /**
     * 根据子 key 获取子 json
     *
     * @param json json 字符串
     * @param key  json 字符串子 key
     * @return 子 json
     */
    public static String get(String json, String key) {

        if (StringUtils.isEmpty(json) || StringUtils.isEmpty(key)) return null;

        String value;
        try {
            value = mapper.readValue(json, JsonNode.class).get(key).textValue();
        } catch (IOException var5) {
            logger.info(var5.getMessage());
            value = null;
        }

        return value;
    }

    /**
     * 将 json 转成 List
     *
     * @param json      json 字符串
     * @param valueType list 泛型
     */
    public static <T> List<T> parseList(String json, Class<T> valueType) {

        return (List<T>) parseCollection(json, List.class, valueType);
    }

    /**
     * 将 json 转成 List
     *
     * @param json      json 字符串
     * @param valueType list 泛型
     */
    public static <T, E extends Collection> Collection<T> parseCollection(String json, Class<E> collectionClass, Class<T> valueType) {

        if (StringUtils.isEmpty(json) || valueType == null) return null;

        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, valueType);

        Collection<T> objectList;
        try {
            objectList = mapper.readValue(json, javaType);
        } catch (Exception e) {
            logger.error("parseList error:{}", e.getMessage(), e);
            objectList = null;
        }

        return objectList;
    }

    /**
     * 将 json 转成指定的类对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(Object obj, Class<T> type) {

        if (StringUtils.isEmpty(obj.toString()) || type == null) return null;
        if (type == String.class) return (T) obj;

        T result;
        try {
            result = mapper.readValue(toJSON(obj), type);
        } catch (Exception e) {
            logger.error("parse error:{}", e.getMessage(), e);
            result = null;
        }

        return result;
    }

}

