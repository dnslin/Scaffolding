package in.dnsl.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleDto {
    // 用户id
    @NotBlank(message = "用户名称不能为空")
    private String username;

    // 角色id
    @NotNull(message = "角色不能为空")
    private List<String> roles;

}
