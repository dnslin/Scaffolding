package in.dnsl.aspect;

import in.dnsl.exception.AppException;
import in.dnsl.repository.UserRepository;
import jakarta.annotation.Resource;
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
public class UserIdCheckAspect {

    @Resource
    private UserRepository repository;

    @Pointcut("@annotation(in.dnsl.annotation.CheckUserId)")
    public void CheckUserId() {}
    @Before("CheckUserId()")
    public void beforeAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long id) {
                boolean b = repository.existsById(id);
                if (!b) throw new AppException("用户不存在或者已被禁用");
            } else {
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("id".equals(field.getName())) {
                        try {
                            field.setAccessible(true);
                            Object value = field.get(arg);
                            if (value instanceof Long id) {
                                boolean b = repository.existsById(id);
                                if (!b) throw new AppException("用户不存在或者已被禁用");
                            }
                        } catch (IllegalAccessException e) {
                            log.error("获取用户ID失败", e);
                        }
                    }
                }
            }
        }
    }

}
