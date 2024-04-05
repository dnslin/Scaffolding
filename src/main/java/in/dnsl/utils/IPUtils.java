package in.dnsl.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class IPUtils {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getClientIp(HttpServletRequest request) {
        Optional<String> clientIp = Arrays.stream(IP_HEADER_CANDIDATES)
                .map(request::getHeader)
                .filter(ip -> ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip))
                .findFirst();
        // 如果 等于 0:0:0:0:0:0:0:1 返回 本地 127.0.0.1
        if ("0:0:0:0:0:0:0:1".equals(clientIp.orElse("")) || clientIp.isEmpty()) return "127.0.0.1";
        return clientIp.orElseGet(request::getRemoteAddr);
    }
}