package in.dnsl.constant;

import lombok.Data;

@Data
public class RedisKeyConstant {

    // 用户token  用户id:token
    public static final String TOKEN_KEY = "PicManger:token:user:%s:token:%s";
}
