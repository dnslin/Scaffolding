package in.dnsl.aspect;

import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.annotation.RateLimiter;
import in.dnsl.annotation.RateLimiters;
import in.dnsl.enums.LimitType;
import in.dnsl.exception.AppException;
import in.dnsl.utils.IPUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static in.dnsl.enums.ResponseEnum.ACCESS_LIMIT;

@Aspect
@Component
public class RateLimiterAspect {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RedisScript<Long> limitScript;

    private final Map<LimitType, KeyGeneratorStrategy> strategyMap = new HashMap<>();

    public RateLimiterAspect() {
        strategyMap.put(LimitType.IP, new IPKeyGeneratorStrategy());
        strategyMap.put(LimitType.USER, new UserKeyGeneratorStrategy());
        // 可以添加更多的限流类型处理
    }

    @Pointcut("@annotation(in.dnsl.annotation.RateLimiter)")
    public void rateLimiter() {}

    @Pointcut("@annotation(in.dnsl.annotation.RateLimiters)")
    public void rateLimiters() {}

    @Before("rateLimiter() || rateLimiters()")
    public void doBefore(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String name = point.getTarget().getClass().getName() + "." + signature.getName();

        List<RateLimiter> limiters = Optional.ofNullable(method.getAnnotation(RateLimiter.class))
                .map(Collections::singletonList)
                .orElseGet(() -> Optional.ofNullable(method.getAnnotation(RateLimiters.class))
                        .map(rateLimiters -> Arrays.asList(rateLimiters.value()))
                        .orElse(Collections.emptyList()));

        if (!allowRequest(limiters, name)) throw new AppException(ACCESS_LIMIT);
    }

    private boolean allowRequest(List<RateLimiter> rateLimiters, String name) {
        List<String> keys = getKeys(rateLimiters, name);
        Object[] args = getArgs(rateLimiters);
        Long result = redisTemplate.execute(limitScript, keys, args);

        return Objects.equals(result, 1L);
    }

    private List<String> getKeys(List<RateLimiter> rateLimiters, String name) {
        return rateLimiters.stream()
                .flatMap(rateLimiter -> Arrays.stream(rateLimiter.rules())
                        .map(rule -> strategyMap.get(rateLimiter.type())
                                .generateKey(rateLimiter, name) + "_" + (rule.time() * 1000) + "_" + rule.count()))
                .collect(Collectors.toList());
    }

    private Object[] getArgs(List<RateLimiter> rateLimiters) {
        List<Object> args = new ArrayList<>();
        args.add(System.currentTimeMillis());
        rateLimiters.forEach(rateLimiter -> Arrays.stream(rateLimiter.rules())
                .forEach(rule -> {
                    args.add(rule.time() * 1000);
                    args.add(rule.count());
                    args.add(UUID.randomUUID().toString());
                }));
        return args.toArray();
    }
}

@FunctionalInterface
interface KeyGeneratorStrategy {
    String generateKey(RateLimiter rateLimiter, String name);
}
@Slf4j
class IPKeyGeneratorStrategy implements KeyGeneratorStrategy {
    @Override
    public String generateKey(RateLimiter rateLimiter, String name) {
        // 实现获取IP地址的逻辑
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String clientIp = IPUtils.getClientIp(request);
        log.info("正在请求的IP是: {}", clientIp);
        return rateLimiter.key() + name + "_IP_" + clientIp;
    }
}

class UserKeyGeneratorStrategy implements KeyGeneratorStrategy {
    @Override
    public String generateKey(RateLimiter rateLimiter, String name) {
        // 实现获取用户ID的逻辑
        long userId = 1L; // 获取用户ID的逻辑
        String string = StpUtil.getLoginIdDefaultNull().toString();
        if (StringUtils.isNotBlank(string)) userId = Long.parseLong(string);
        return rateLimiter.key() + name + "_USER_" + userId;
    }
}
