package in.dnsl.enums;

import lombok.Getter;


@Getter
public enum ResponseEnum {
    SUCCESS("200", "success"),
    SYSTEM_ERROR("500", "发生未知异常。。。"),
    // 访问系统频繁
    ACCESS_LIMIT("1001", "访问系统频繁，请稍后再试"),
    //"用户不存在或者被禁用"
    USER_NOT_EXIST("1002", "用户不存在或者被禁用"),
    //token不存在
    TOKEN_NOT("1003", "Token错误或者不存在或者已过期"),
    //token名称不允许重复
    TOKEN_NAME("1004", "token名称不允许重复"),
    // 网站配置信息不存在
    CONFIG_NOT_EXIST("1005", "网站配置信息不存在"),
    // 上传配置未初始化
    UPLOAD_CONFIG_NOT_INIT("1006", "上传配置未初始化"),
    // 用户或者密码错误
    USER_OR_PASSWORD_ERROR("1007", "用户名或者密码错误"),
    // 用户已存在
    USER_EXIST("1008", "用户已存在"),
    // 用户不存在 或者 已经被禁用
    USER_OR_DISABLE_ERROR("1009", "用户不存在或者被禁用"),
    // 角色不存在
    ROLE_NOT_EXIST("1010", "角色不存在");

    private final String code;
    private final String message;

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
    }