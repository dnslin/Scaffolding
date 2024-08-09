package in.dnsl.service;

import in.dnsl.enums.CategorizationType;
import in.dnsl.exception.AppException;
import in.dnsl.model.entity.UploadConfiguration;
import in.dnsl.repository.UploadConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static in.dnsl.enums.ResponseEnum.UPLOAD_CONFIG_NOT_INIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadConfigurationService {

    private final UploadConfigurationRepository repository;


    /**
     * 获取当前的上传配置
     * 如果不存在配置,则创建一个默认配置
     */
    @Transactional
    @Cacheable(value = "PicManager:Upload:cache:config", key = "'1'")
    public UploadConfiguration getCurrentConfiguration() {
        return repository.findById(1L)
                .orElseGet(this::createDefaultConfiguration);
    }


    @Transactional(rollbackOn = Exception.class)
    @CachePut(value = "PicManager:Upload:cache:config", key = "#result.id")
    public UploadConfiguration updateConfiguration(UploadConfiguration configuration) {
        UploadConfiguration existing = repository.findById(1L).orElseThrow(()->
                new AppException(UPLOAD_CONFIG_NOT_INIT));
        // 更新现有配置的字段,保留ID和创建时间
        configuration.setId(existing.getId());
        configuration.setCreatedAt(existing.getCreatedAt());
        return repository.save(configuration);
    }


    /**
     * 创建默认配置
     * @return 默认的上传配置
     */
    private UploadConfiguration createDefaultConfiguration() {
        UploadConfiguration defaultConfig = new UploadConfiguration();
        defaultConfig.setId(1L);
        defaultConfig.setStorageDirectory("/uploads");
        defaultConfig.setDefaultCategorization(CategorizationType.TIME_BASED);
        defaultConfig.setAllowedFileFormats(List.of("jpg", "png", "gif"));
        defaultConfig.setMaxUploadCount(10);
        // 设置其他默认值...
        return repository.save(defaultConfig);
    }

    /**
     * 检查文件格式是否被允许
     * @param fileFormat 文件格式
     * @return 如果文件格式被允许返回true,否则返回false
     */
    public boolean isFileFormatAllowed(String fileFormat) {
        UploadConfiguration config = repository.findFirstByOrderByIdAsc().orElseGet(UploadConfiguration::new);
        return config.getAllowedFileFormats().contains(fileFormat.toLowerCase());
    }

    /**
     * 获取文件的转换格式列表
     * @param sourceFormat 源文件格式
     * @return 可转换的格式列表,如果没有可转换的格式则返回空列表
     */
    public List<String> getConversionFormats(String sourceFormat) {
        UploadConfiguration config = repository.findFirstByOrderByIdAsc().orElseGet(UploadConfiguration::new);
        return config.getFileConversionFormats().getOrDefault(sourceFormat.toLowerCase(), List.of());
    }

    /**
     * 检查图片尺寸是否在允许的范围内
     * @param width 图片宽度
     * @param height 图片高度
     * @return 如果图片尺寸在允许的范围内返回true,否则返回false
     */
    public boolean isImageSizeAllowed(int width, int height) {
        UploadConfiguration config = repository.findFirstByOrderByIdAsc().orElseGet(UploadConfiguration::new);
        return (config.getMinImageWidth() == null || width >= config.getMinImageWidth()) &&
                (config.getMaxImageWidth() == null || width <= config.getMaxImageWidth()) &&
                (config.getMinImageHeight() == null || height >= config.getMinImageHeight()) &&
                (config.getMaxImageHeight() == null || height <= config.getMaxImageHeight());
    }


}
