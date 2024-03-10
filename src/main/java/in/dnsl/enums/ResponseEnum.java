package in.dnsl.enums;

import lombok.Getter;


@Getter
public enum ResponseEnum {
    SUCCESS("200","success"),
    SYSTEM_ERROR("500","发生未知异常。。。");

    private final String code;
    private final String message;

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}