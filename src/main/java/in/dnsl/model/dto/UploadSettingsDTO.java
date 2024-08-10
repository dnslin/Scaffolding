package in.dnsl.model.dto;

import in.dnsl.enums.CategorizationType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UploadSettingsDTO {

    @NotBlank(message = "存储目录不能为空")
    private String storageDirectory;

    @NotNull(message = "默认分类方式不能为空")
    private CategorizationType defaultCategorization;

    @NotEmpty(message = "允许的文件格式列表不能为空")
    private List<String> allowedFileFormats;

    private Map<String, List<String>> fileConversionFormats;

    @Min(value = 1, message = "单次上传最大数量必须大于0")
    private Integer maxUploadCount;

    @Positive(message = "图片最大宽度必须是正数")
    private Integer maxImageWidth;

    @Positive(message = "图片最大高度必须是正数")
    private Integer maxImageHeight;

    @Positive(message = "图片最小宽度必须是正数")
    private Integer minImageWidth;

    @Positive(message = "图片最小高度必须是正数")
    private Integer minImageHeight;

    @AssertTrue(message = "最小图片尺寸不能大于最大图片尺寸")
    public boolean isImageSizeValid() {
        return (minImageWidth == null || maxImageWidth == null || minImageWidth <= maxImageWidth) &&
                (minImageHeight == null || maxImageHeight == null || minImageHeight <= maxImageHeight);
    }
}