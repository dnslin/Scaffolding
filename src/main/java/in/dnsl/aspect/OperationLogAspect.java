package in.dnsl.aspect;

import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.annotation.LogOperation;
import in.dnsl.enums.ActionType;
import in.dnsl.model.entity.OperationLog;
import in.dnsl.repository.OperationLogRepository;
import in.dnsl.utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);
    private final OperationLogRepository repository;

    @Pointcut("@annotation(in.dnsl.annotation.LogOperation)")
    public void logOperationPointcut() {}


    @Around("logOperationPointcut()")
    public Object aroundLogOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogOperation logOperation = signature.getMethod().getAnnotation(LogOperation.class);
        ActionType actionType = logOperation.actionType();
        String description = logOperation.description();
        // 获取用户IP
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String clientIp = IPUtils.getClientIp(request);
        // 获取当前用户 如果为空则为匿名用户
        Object loginIdAndName = StpUtil.getLoginIdDefaultNull();
        String idAndName = Optional.ofNullable(loginIdAndName)
                .map(Object::toString)
                .orElseGet(() -> "0:" + clientIp + "_Anonymous");
        String[] strList = idAndName.split(":");
        Long id = Long.parseLong(strList[0]);
        String username = strList[1];
        // 记录操作日志
        OperationLog build = OperationLog.builder()
                .operationIp(clientIp)
                .userName(username)
                .operationType(actionType.name())
                .operationDetails(description)
                .userId(id).build();
        OperationLog save = repository.save(build);
        log.info("操作日志记录成功：{}", save.getOperationDetails());
        // 继续执行原方法
        return joinPoint.proceed();
    }

}
