package in.dnsl.aspect;

import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.annotation.LogRedisUsername;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
@Slf4j
@Aspect
@Component
public class LogRedisUsernameAspect {

    @Pointcut("@annotation(logRedisUsername)")
    public void logRedisUsernamePointcut(LogRedisUsername logRedisUsername) {}

    @AfterReturning(value = "logRedisUsernamePointcut(logRedisUsername)", argNames = "joinPoint,logRedisUsername")
    public void logUsername(JoinPoint joinPoint, LogRedisUsername logRedisUsername) {
        String loginIdAndUserName = StpUtil.getLoginId().toString();
        // 通过 ： 切分 id和用户名
        String[] split = loginIdAndUserName.split(":");
        String username = split[1];
        String userId = split[0];
        // 记录日志到登录日志表

        // 定义 登录日志为  type=1,上传日志为type=2,请求日志为type=3

        // 这里可以进行日志记录或其他操作
    }
}
