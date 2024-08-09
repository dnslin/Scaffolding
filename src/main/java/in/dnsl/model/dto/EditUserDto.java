package in.dnsl.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditUserDto {


    @Email(message = "请填写正确的邮箱地址")
    @Size(max = 30, message = "邮箱长度最大为30个字符")
    private String email;

    @Size(max = 28, message = "昵称长度最大为28个字符")
    private String nickname;

    private String avatar;

}
