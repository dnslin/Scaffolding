package in.dnsl.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

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
            log.error("无法访问字段: {}", sourceField.getName(), e);
        }
    }

    private static void updateTargetField(Field sourceField, Object value, Object target) {
        try {
            Field targetField = target.getClass().getDeclaredField(sourceField.getName());
            targetField.setAccessible(true);
            targetField.set(target, value);
        } catch (NoSuchFieldException e) {
            log.error("目标对象中不存在字段: {}", sourceField.getName());
        } catch (IllegalAccessException e) {
            log.error("无法设置目标字段值: {}", sourceField.getName(), e);
        }
    }
}
