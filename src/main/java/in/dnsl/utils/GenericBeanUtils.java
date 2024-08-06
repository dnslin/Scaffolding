package in.dnsl.utils;

import org.springframework.beans.BeanUtils;

public class GenericBeanUtils {
    public static <S, T> T copyProperties(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("从源到目标复制属性失败", e);
        }
    }

    public static <S, T> void copyProperties(S source, T target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target);
    }
}
