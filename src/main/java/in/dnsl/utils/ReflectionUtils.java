package in.dnsl.utils;

import in.dnsl.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * 反射工具类
 */
@Slf4j
public class ReflectionUtils {

    public static void updateFieldsIfPresent(Object source, Object target) {
        Optional.ofNullable(source)
                .map(Object::getClass)
                .map(Class::getDeclaredFields)
                .stream()
                .flatMap(Arrays::stream)
                .forEach(field -> updateFieldValue(field, source, target));
    }

    private static void updateFieldValue(Field sourceField, Object source, Object target) {
        try {
            sourceField.setAccessible(true);
            Optional.ofNullable(sourceField.get(source))
                    .filter(value -> !value.toString().isEmpty())
                    .ifPresent(value -> updateTargetField(sourceField, value, target));
        } catch (IllegalAccessException e) {
            throw new AppException("无法访问字段: " + sourceField.getName());
        }
    }

    private static void updateTargetField(Field sourceField, Object value, Object target) {
        try {
            Field targetField = target.getClass().getDeclaredField(sourceField.getName());
            targetField.setAccessible(true);
            targetField.set(target, value);
        } catch (NoSuchFieldException e) {
            throw new AppException("目标对象中不存在字段: " + sourceField.getName());
        } catch (IllegalAccessException e) {
            throw new AppException("无法设置目标字段值: " + sourceField.getName());
        }
    }
}
