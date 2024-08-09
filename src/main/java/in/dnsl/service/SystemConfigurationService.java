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


    @Cacheable(value = "PicManager:Config:cache", key = "'system'")
    public SystemConfiguration getConfiguration() {
        return repository.findById(1L)
                .orElseGet(() -> repository.save(new SystemConfiguration()));
    }

    @CachePut(value = "PicManager:Config:cache", key = "'system'")
    @Transactional(rollbackOn = Exception.class)
    public SystemConfiguration updateConfiguration(SystemConfiguration configuration) {
        configuration.setId(1); // 确保ID始终为1
        return repository.save(configuration);
    }

}
