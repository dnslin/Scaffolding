package in.dnsl.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestPassDTO {

    @NotBlank(message = "密码不能为空")
    private  String password;
    @NotBlank(message = "旧密码不能为空")
    private  String oldPassword;
}
