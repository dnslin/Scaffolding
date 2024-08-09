package in.dnsl.service;

import in.dnsl.exception.AppException;
import in.dnsl.model.dto.SiteConfigDto;
import in.dnsl.model.entity.SiteConfig;
import in.dnsl.repository.SiteConfigRepository;
import in.dnsl.utils.GenericBeanUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static in.dnsl.enums.ResponseEnum.CONFIG_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteConfigService {

    private final SiteConfigRepository repository;

    @Cacheable(value = "PicManager:SiteConfig:cache:siteConfig", key = "'Pic'")
    public SiteConfigDto getSiteConfig() {
        log.info("获取网站的配置信息...");
        SiteConfig config = repository.findSiteConfigById(1L).orElseThrow(
                () -> new AppException(CONFIG_NOT_EXIST)
        );
        return GenericBeanUtils.copyProperties(config, SiteConfigDto.class,false);
    }

    @Transactional
    @CachePut(value = "PicManager:SiteConfig:cache:siteConfig", key = "'Pic'")
    public SiteConfigDto updateSiteConfig(SiteConfig siteConfig) {
        log.info("更新网站的配置信息...");
        // 默认只有一条数据 所以id为1L
        siteConfig.setId(1L);
        SiteConfig save = repository.save(siteConfig);
        return GenericBeanUtils.copyProperties(save, SiteConfigDto.class,false);
    }
}
