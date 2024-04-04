package in.dnsl.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private BlacklistInterceptor blacklistInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("注册拦截器已经生效......");
        registry.addInterceptor(blacklistInterceptor); // 注册拦截器
    }
}