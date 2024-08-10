package in.dnsl.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotNull(message = "状态不能为空")
    private Boolean disable;
}
