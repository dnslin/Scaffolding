package in.dnsl.constant;

import lombok.Data;

@Data
public class RedisKeyConstant {

    // 用户token  用户id:token
    public static final String TOKEN_KEY = "PicManger:token:User:%s:token:%s";


    // 用户信息  用户id
    public static final String USER_INFO_KEY = "PicManger:User:info:%s";


    // 请求IP黑名单
    public static final String IP_BLACKLIST = "PicManger:IP:blacklist";
}
