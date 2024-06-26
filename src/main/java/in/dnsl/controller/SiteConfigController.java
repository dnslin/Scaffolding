package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import in.dnsl.annotation.LogOperation;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.enums.ActionType;
import in.dnsl.model.dto.SiteConfigDto;
import in.dnsl.model.entity.SiteConfig;
import in.dnsl.service.SiteConfigService;
import in.dnsl.utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/token/siteConfig")
public class SiteConfigController {

    private final SiteConfigService service;

    // 获取网站的配置信息
    @SaIgnore
    @GetMapping("/getSiteConfig")
    public Wrapper<SiteConfigDto> getSiteConfig(HttpServletRequest request) {
        String clientIp = IPUtils.getClientIp(request);
        log.info("获取网站的配置信息... 客户端IP地址: {}", clientIp);
        SiteConfigDto config = service.getSiteConfig();
        return WrapMapper.ok(config);
    }


    // 更新网站的配置信息
    @SaCheckLogin
    @GetMapping("/updateSiteConfig")
    @LogOperation(description = "更新网站的配置信息", actionType = ActionType.MODIFY)
    public Wrapper<?> updateSiteConfig(@RequestBody SiteConfig siteConfig) {
        log.info("更新网站的配置信息...");
        service.updateSiteConfig(siteConfig);
        return WrapMapper.ok();
    }
}
