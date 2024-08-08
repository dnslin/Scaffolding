package in.dnsl.utils;

import in.dnsl.model.dto.AccountInfoDto;

import java.util.Optional;

public class Common {

    // 根据 Sa-token 的配置，获取当前登录的账号 和 用户ID
    public static AccountInfoDto getLoginAccount(Object o) {
        return Optional.ofNullable(o)
                .map(Object::toString)
                .map(info -> info.split(":"))
                .filter(arr -> arr.length == 2)
                .map(arr -> AccountInfoDto.builder().userId(Long.parseLong(arr[0])).username(arr[1]).build())
                .orElseThrow(() -> new RuntimeException("获取登录用户信息失败"));
    }
}
