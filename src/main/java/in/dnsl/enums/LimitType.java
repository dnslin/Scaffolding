package in.dnsl.enums;

/**
 * @Description : 限流类型
 **/
public enum LimitType {
    /**
     * 根据请求者IP进行限流
     */
    IP,

    /**
     * 根据用户限流
     */
    USER,
}