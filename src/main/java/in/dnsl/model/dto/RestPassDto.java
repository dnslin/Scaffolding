package in.dnsl.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestPassDto {

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private  String password;
    @NotBlank(message = "旧密码不能为空")
    private  String oldPassword;
}
