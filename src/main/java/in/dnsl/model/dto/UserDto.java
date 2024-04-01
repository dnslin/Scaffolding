package in.dnsl.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 128, message = "用户名长度最大为128个字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请填写正确的邮箱地址")
    @Size(max = 30, message = "邮箱长度最大为30个字符")
    private String email;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 28, message = "昵称长度最大为28个字符")
    private String nickname;

    private String avatar;

    private String lastLoginIp;
}
