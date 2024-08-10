package in.dnsl.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.dnsl.enums.CategorizationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "upload_configuration")
public class UploadConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 存储目录路径
    @Column(name = "storage_directory", nullable = false)
    private String storageDirectory;

    // 默认分类方式（枚举类型）
    @Enumerated(EnumType.STRING)
    @Column(name = "default_categorization", nullable = false)
    private CategorizationType defaultCategorization;

    // 允许的文件格式列表，使用自定义转换器
    @Convert(converter = StringListConverter.class)
    @Column(name = "allowed_file_formats", nullable = false)
    private List<String> allowedFileFormats;

    // 文件格式转换映射，使用自定义转换器
    @Convert(converter = FileConversionMapConverter.class)
    @Column(name = "file_conversion_formats")
    private Map<String, List<String>> fileConversionFormats;

    // 单次上传最大数量限制
    @Min(1)
    @Column(name = "max_upload_count", nullable = false)
    private Integer maxUploadCount;

    // 图片最大宽度
    @Positive(message = "图片最大宽度 必须是正数")
    @Column(name = "max_image_width")
    private Integer maxImageWidth;

    // 图片最大高度
    @Positive(message = "图片最大高度 必须是正数")
    @Column(name = "max_image_height")
    private Integer maxImageHeight;

    // 图片最小宽度
    @Positive(message = "图片最小宽度 必须是正数")
    @Column(name = "min_image_width")
    private Integer minImageWidth;

    // 图片最小高度
    @Positive(message = "图片最小高度 必须是正数")
    @Column(name = "min_image_height")
    private Integer minImageHeight;

    @Column(name = "file_size")
    private Integer fileSize;

    // 记录创建时间
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 记录更新时间
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 省略 Getters 和 Setters...

    // 创建记录时自动设置时间戳
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // 更新记录时自动更新时间戳
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Converter
    public static class StringListConverter implements AttributeConverter<List<String>, String> {
        private static final String SPLIT_CHAR = ",";

        // 将 List<String> 转换为数据库列
        @Override
        public String convertToDatabaseColumn(List<String> attribute) {
            return attribute != null ? String.join(SPLIT_CHAR, attribute) : null;
        }

        // 将数据库列转换为 List<String>
        @Override
        public List<String> convertToEntityAttribute(String dbData) {
            return dbData != null ? Arrays.asList(dbData.split(SPLIT_CHAR)) : null;
        }
    }


    @Converter
    public static class FileConversionMapConverter implements AttributeConverter<Map<String, List<String>>, String> {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        // 将 Map<String, List<String>> 转换为 JSON 字符串
        @Override
        public String convertToDatabaseColumn(Map<String, List<String>> attribute) {
            try {
                return attribute != null ? objectMapper.writeValueAsString(attribute) : null;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("将映射转换为 JSON 时出错", e);
            }
        }

        // 将 JSON 字符串转换为 Map<String, List<String>>
        @Override
        public Map<String, List<String>> convertToEntityAttribute(String dbData) {
            try {
                return dbData != null ? objectMapper.readValue(dbData, new TypeReference<>() {
                }) : null;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("将 JSON 转换为映射时出错", e);
            }
        }
    }


}