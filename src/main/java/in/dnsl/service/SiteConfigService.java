package in.dnsl.service;

import in.dnsl.exception.AppException;
import in.dnsl.model.entity.SiteConfig;
import in.dnsl.repository.SiteConfigRepository;
import in.dnsl.utils.ReflectionUtils;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SiteConfigService {

    @Resource
    private SiteConfigRepository repository;

    public SiteConfig getSiteConfig() {
        log.info("获取网站的配置信息...");
        return repository.findSiteConfigById(1L).orElseThrow(
                () -> new AppException("网站配置信息不存在")
        );
    }

    @Transactional
    public void updateSiteConfig(SiteConfig siteConfig) {
        log.info("更新网站的配置信息...");
        SiteConfig config = repository.findSiteConfigById(1L).orElseThrow(
                () -> new AppException("网站配置信息不存在")
        );
        ReflectionUtils.updateFieldsIfPresent(config, siteConfig);
        repository.save(config);
    }
}
