package in.dnsl.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Map;

public class GenericBeanUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 通用的拷贝方法，支持深拷贝和浅拷贝
     * @param source 源对象
     * @param targetClass 目标类
     * @param deepCopy 是否进行深拷贝
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     * @return 拷贝后的对象
     */
    public static <S, T> T copyProperties(S source, Class<T> targetClass, boolean deepCopy) {
        if (source == null) {
            return null;
        }
        try {
            if (deepCopy) {
                return deepCopyInternal(source, targetClass);
            } else {
                T target = targetClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, target);
                return target;
            }
        } catch (Exception e) {
            throw new RuntimeException("从源到目标复制属性失败", e);
        }
    }

    /**
     * 深拷贝方法
     * @param source 源对象
     * @param <T> 对象类型
     * @return 深拷贝后的对象
     */
    public static <T> T deepCopy(T source) {
        return deepCopyInternal(source, (Class<T>)source.getClass());
    }

    private static <S, T> T deepCopyInternal(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        if (source instanceof Collection || source instanceof Map) {
            return objectMapper.convertValue(source, targetClass);
        }

        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(source), targetClass);
        } catch (Exception e) {
            throw new RuntimeException("深拷贝失败", e);
        }
    }
}
