package in.dnsl.annotation;

import in.dnsl.enums.LimitType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(value = RateLimiters.class)
public @interface RateLimiter {

    /**
     * 限流键前缀
     */
    String key() default "rate_limit:";

    /**
     * 限流规则
     */
    RateLimitRule[] rules() default {};

    /**
     * 限流类型
     *
     */
    LimitType type() default LimitType.IP;
}