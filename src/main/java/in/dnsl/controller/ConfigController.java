package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import in.dnsl.annotation.LogOperation;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.enums.ActionType;
import in.dnsl.model.dto.SiteConfigDto;
import in.dnsl.model.entity.SiteConfig;
import in.dnsl.model.entity.SystemConfiguration;
import in.dnsl.service.SiteConfigService;
import in.dnsl.service.SystemConfigurationService;
import in.dnsl.utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/config")
public class ConfigController {

    private final SiteConfigService service;

    private final SystemConfigurationService systemService;
    // 获取网站的配置信息
    @SaIgnore
    @GetMapping("/site/get")
    public Wrapper<SiteConfigDto> getSiteConfig(HttpServletRequest request) {
        String clientIp = IPUtils.getClientIp(request);
        log.info("获取网站的配置信息---客户端IP地址: {}", clientIp);
        SiteConfigDto config = service.getSiteConfig();
        return WrapMapper.ok(config);
    }


    // 更新网站的配置信息
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/site/update")
    @LogOperation(description = "更新网站的配置信息", actionType = ActionType.MODIFY)
    public Wrapper<SiteConfigDto> updateSiteConfig(@RequestBody SiteConfig siteConfig) {
        SiteConfigDto siteConfigDto = service.updateSiteConfig(siteConfig);
        return WrapMapper.ok(siteConfigDto);
    }

    // 系统配置
    @SaCheckLogin
    @GetMapping("/system/get")
    public Wrapper<SystemConfiguration> getSystemConfig() {
        SystemConfiguration configuration = systemService.getConfiguration();
        return WrapMapper.ok(configuration);
    }

    @SaCheckLogin
    @PostMapping("/system/update")
    @SaCheckRole("admin")
    public ResponseEntity<SystemConfiguration> updateConfiguration(@RequestBody SystemConfiguration configuration) {
        SystemConfiguration updatedConfig = systemService.updateConfiguration(configuration);
        return ResponseEntity.ok(updatedConfig);
    }
}
