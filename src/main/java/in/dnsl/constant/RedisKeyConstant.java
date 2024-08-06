package in.dnsl.constant;

import lombok.Data;

@Data
public class RedisKeyConstant {

    // 用户token  用户id:token
    public static final String TOKEN_KEY = "PicManager:token:User:%s:token:%s";


    // 用户信息  用户id
    public static final String USER_INFO_KEY = "PicManager:User:info:%s";


    // 请求IP黑名单
    public static final String IP_BLACKLIST = "PicManager:IP:blacklist";

    // 用户信息缓存
    public static final String USER_INFO_CACHE = "PicManager:User:cache:userInfo";

    // 网站配置信息缓存
    public static final String SITE_CONFIG_CACHE = "PicManager:SiteConfig:cache:siteConfig::Pic";
}
