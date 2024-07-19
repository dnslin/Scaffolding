package in.dnsl.aspect;

import in.dnsl.exception.AppException;
import in.dnsl.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserCheckAspect {

    private final UserRepository repository;

    @Pointcut("@annotation(in.dnsl.annotation.CheckUserName)")
    public void CheckUserName() {}

    @Before("CheckUserName()")
    public void beforeAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof String username) {
                boolean b = repository.existsByUsername(username);
                if (!b) throw new AppException("用户不存在或者已被禁用");
            } else {
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("username".equals(field.getName())) {
                        try {
                            field.setAccessible(true);
                            Object value = field.get(arg);
                            if (value instanceof String username) {
                                boolean b = repository.existsByUsername(username);
                                if (!b) throw new AppException("用户不存在或者已被禁用");
                            }
                        } catch (IllegalAccessException e) {
                            log.error("获取username失败", e);
                        }
                    }
                }
            }
        }
    }

}
