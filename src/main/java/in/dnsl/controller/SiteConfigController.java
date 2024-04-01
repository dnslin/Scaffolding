package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import in.dnsl.service.SiteConfigService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/token/siteConfig")
public class SiteConfigController {

    @Resource
    private SiteConfigService siteConfigService;

    // 获取网站的配置信息
    @GetMapping("/getSiteConfig")
    public void getSiteConfig() {
        log.info("获取网站的配置信息...");
    }


    // 更新网站的配置信息
    @SaIgnore
    @GetMapping("/updateSiteConfig")
    public void updateSiteConfig() {
        log.info("更新网站的配置信息...");
    }
}
