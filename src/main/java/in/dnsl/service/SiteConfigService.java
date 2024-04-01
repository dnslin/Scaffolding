package in.dnsl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SiteConfigService {

    public void getSiteConfig() {
        log.info("获取网站的配置信息...");
    }

    public void updateSiteConfig() {
        log.info("更新网站的配置信息...");
    }
}
