package in.dnsl.config;

import in.dnsl.constant.RedisKeyConstant;
import in.dnsl.repository.UserRepository;
import in.dnsl.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisInitializer implements CommandLineRunner {

    private final RedisUtils redisUtils;

    private final UserRepository repository;
    @Override
    public void run(String... args) {
        repository.findAllByEnabled(true).forEach(user -> {
            redisUtils.set(String.format(RedisKeyConstant.USER_INFO_KEY, user.getId()), user.getUsername());
            log.info("用户信息缓存成功: {}", user.getUsername());
        });
        // TODO 初始化 黑名单 到 redis内 使用 set 结构
        // TODO 初始化系统配置 到 redis内 使用 hash 结构
    }
}
