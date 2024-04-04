package in.dnsl.service;

import in.dnsl.exception.AppException;
import in.dnsl.model.dto.SiteConfigDto;
import in.dnsl.model.entity.SiteConfig;
import in.dnsl.repository.SiteConfigRepository;
import in.dnsl.utils.ReflectionUtils;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteConfigService {

    private final SiteConfigRepository repository;

    public SiteConfigDto getSiteConfig() {
        log.info("获取网站的配置信息...");
        SiteConfig config = repository.findSiteConfigById(1L).orElseThrow(
                () -> new AppException("网站配置信息不存在")
        );
        SiteConfigDto siteConfigDto = new SiteConfigDto();
        ReflectionUtils.updateFieldsIfPresent(config, siteConfigDto);
        return siteConfigDto;
    }

    @Transactional
    public void updateSiteConfig(SiteConfig siteConfig) {
        log.info("更新网站的配置信息...");
        // 默认只有一条数据 所以id为1L
        siteConfig.setId(1L);
        repository.save(siteConfig);
    }
}
