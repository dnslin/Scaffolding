package in.dnsl.service;

import in.dnsl.model.entity.SystemConfiguration;
import in.dnsl.repository.SystemConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigurationService {

    private final SystemConfigurationRepository repository;


    @Cacheable(value = "systemConfig", key = "'systemConfig'")
    public SystemConfiguration getConfiguration() {
        return repository.findFirstByOrderByIdAsc();
    }

    @CachePut(key = "'config'")
    @Transactional(rollbackOn = Exception.class)
    public SystemConfiguration updateConfiguration(SystemConfiguration configuration) {
        SystemConfiguration existingConfig = getConfiguration();
        if (existingConfig == null) {
            return repository.save(configuration);
        } else {
            // 更新现有配置的字段
            BeanUtils.copyProperties(configuration, existingConfig, "id");
            return repository.save(existingConfig);
        }
    }
}
