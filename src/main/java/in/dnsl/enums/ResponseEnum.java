package in.dnsl.enums;

public enum ResponseEnum {

    SUCCESS("200","success"),
    SYSTEM_ERROR("500","发生未知异常。。。");

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}