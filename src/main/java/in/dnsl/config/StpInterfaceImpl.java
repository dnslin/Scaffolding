package in.dnsl.config;

import cn.dev33.satoken.stp.StpInterface;
import in.dnsl.model.entity.Permission;
import in.dnsl.model.entity.Role;
import in.dnsl.service.PermissionService;
import in.dnsl.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserService userService;

    private final PermissionService permissionService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = getUserId(loginId.toString());
        // 根据用户id查询用户的权限列表
        Set<Permission> permissionsSet = permissionService.getPermissionsByUserId(userId);
        return permissionsSet.stream()
                .map(Permission::getName)
                .toList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = getUserId(loginId.toString());
        // 根据用户id查询用户的角色列表
        Set<Role> roleSet = userService.getRolesByUserId(userId);
        return roleSet.stream()
                .map(Role::getName)
                .toList();
    }

    // 因为 loginId是 用户id:用户名 所以进行切分
    private Long getUserId(String loginId) {
        return Long.parseLong(loginId.split(":")[0]);
    }
}