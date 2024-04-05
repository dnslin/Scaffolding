package in.dnsl.annotation;

import in.dnsl.enums.ActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {
    ActionType actionType(); // 指定操作类型
    String description() default ""; // 提供操作描述，默认为空
}
