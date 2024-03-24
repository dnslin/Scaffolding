package in.dnsl.annotation;

public @interface RateLimitRule {

    /**
     * 时间窗口, 单位秒
     */
    int time() default 60;

    /**
     * 允许请求数
     */
    int count() default 100;
}