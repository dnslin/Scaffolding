package in.dnsl.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final BlacklistInterceptor blacklistInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("注册拦截器已经生效......");
        registry.addInterceptor(blacklistInterceptor); // 注册拦截器
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        log.info("注册权限注解拦截器......");
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }
}