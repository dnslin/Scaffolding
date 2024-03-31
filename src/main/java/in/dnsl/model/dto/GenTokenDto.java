package in.dnsl.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenTokenDto {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotBlank(message = "token名称不能为空")
    private String tokenName;

    @NotNull(message = "有效天数不能为空")
    private Integer day;
}
