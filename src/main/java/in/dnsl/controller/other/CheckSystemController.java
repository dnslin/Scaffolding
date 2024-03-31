package in.dnsl.controller.other;

import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
public class CheckSystemController {

    private final RedisUtils redisUtils;
    @GetMapping("/api/redis-health")
    public Wrapper<?> checkRedisHealth() {
        try {
            redisUtils.set("redis-health-check", "OK");
        } catch (Exception e) {
            return WrapMapper.error("Redis health check failed.");
        }
        return WrapMapper.ok("Redis health check passed.");
    }
}
