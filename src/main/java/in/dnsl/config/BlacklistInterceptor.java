package in.dnsl.config;

import in.dnsl.constant.RedisKeyConstant;
import in.dnsl.utils.IPUtils;
import in.dnsl.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlacklistInterceptor implements HandlerInterceptor {

    private final RedisUtils redisUtils;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String clientIp = IPUtils.getClientIp(request);
        // 从redis中获取黑名单
        Set<String> blackIpSet = redisUtils.setMembers(RedisKeyConstant.IP_BLACKLIST);
        if (blackIpSet.contains(clientIp)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied"); // 如果IP在黑名单中，返回403错误
            log.error("IP地址 {} 在黑名单中，禁止访问", clientIp);
            return false; // 阻止进一步的处理
        }
        return true; // IP不在黑名单中，继续处理请求
    }
}