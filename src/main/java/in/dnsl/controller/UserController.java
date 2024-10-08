package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.annotation.LogOperation;
import in.dnsl.annotation.RateLimitRule;
import in.dnsl.annotation.RateLimiter;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.enums.ActionType;
import in.dnsl.enums.LimitType;
import in.dnsl.model.dto.*;
import in.dnsl.model.vo.UserInfoVo;
import in.dnsl.service.UserService;
import in.dnsl.utils.Common;
import in.dnsl.utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * @Title: createUser
     * @Description: 创建用户
     * @param: UserVo
     * @return: Wrapper
     * @author: DnsLin
     * @date: 2024/3/23 14:47
     */
    @SaIgnore
    @RateLimiter(rules = {@RateLimitRule, @RateLimitRule(time = 10, count = 50)})
    @RateLimiter(rules = {@RateLimitRule(time = 1, count = 2)}, type = LimitType.IP)
    @PostMapping("/create")
    @LogOperation(description = "创建用户", actionType = ActionType.MODIFY)
    public Wrapper<?> createUser(@Validated @RequestBody UserDTO info, HttpServletRequest request) {
        log.info("创建用户...");
        String ipAddress = IPUtils.getClientIp(request);
        info.setLastLoginIp(ipAddress);
        userService.createUser(info);
        return WrapMapper.ok();
    }

    /**
     * @Title: doLogin
     * @Description: 登录
     * @param: loginDTO
     * @return: Wrapper<?>
     * @author: DnsLin
     * @date: 2024/3/24 16:25
     */
    @SaIgnore
    @RateLimiter(rules = {@RateLimitRule, @RateLimitRule(time = 10, count = 50)})
    @RateLimiter(rules = {@RateLimitRule(time = 1, count = 2)}, type = LimitType.IP)
    @PostMapping("/login")
    public Wrapper<UserInfoVo> doLogin(@Validated @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        log.info("用户登录...");
        String ipAddress = IPUtils.getClientIp(request);
        loginDTO.setLastLoginIp(ipAddress);
        loginDTO.setUA(request.getHeader("User-Agent"));
        return WrapMapper.ok(userService.doLogin(loginDTO));
    }

    /**
     * @Title: doLogout
     * @Description: 注销
     * @return: Wrapper
     * @author: DnsLin
     * @date: 2024/3/24 16:25
     */
    @SaCheckLogin
    @GetMapping("/logout")
    @LogOperation(description = "用户注销", actionType = ActionType.LOGOUT)
    public Wrapper<?> doLogout() {
        // 根据用户名注销
        StpUtil.logout();
        return WrapMapper.ok();
    }

    /**
     * @Title: getUserInfo
     * @Description: 查看用户信息
     * @param: username
     * @return: UserInfoVo
     * @author: DnsLin
     * @date: 2024/3/24 16:26
     */
    @SaCheckLogin
    @GetMapping("/info")
    public Wrapper<UserInfoVo> getUserInfo() {
        AccountInfoDTO account = Common.getLoginAccount(StpUtil.getLoginId());
        return WrapMapper.ok(userService.getUserInfo(account.getUsername()));
    }


    /**
     * @Title: updateUserInfo
     * @Description: 修改用户信息
     * @param: UserVo
     * @return: WrapMapper
     * @date: 2024/3/24 16:26
     */
    @SaCheckLogin
    @PostMapping("/update")
    @LogOperation(description = "修改用户信息", actionType = ActionType.MODIFY)
    public Wrapper<UserInfoVo> updateUserInfo(@Validated @RequestBody EditUserDTO info) {
        AccountInfoDTO account = Common.getLoginAccount(StpUtil.getLoginIdAsLong());
        return WrapMapper.ok(userService.updateUserInfo(info, account));
    }


    /**
     * @Title: deleteUser
     * @Description: 删除用户
     * @param: username
     * @return: Wrapper
     * @author: DnsLin
     * @date: 2024/3/24 16:26
     */
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/delete")
    @LogOperation(description = "删除用户", actionType = ActionType.MODIFY)
    public Wrapper<?> deleteUser(String username) {
        userService.deleteUser(username);
        return WrapMapper.ok();
    }

    /**
     * @Title: disableUser
     * @Description: 禁用用户/启用用户
     * @param: username
     * @param: disable
     * @return: WrapMapper
     * @author: DnsLin
     * @date: 2024/3/24 16:26
     */
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/disable")
    @LogOperation(description = "禁用用户", actionType = ActionType.MODIFY)
    public Wrapper<?> disableUser(@RequestBody @Validated UserStatusDTO userStatusDto) {
        log.info("用户{}禁用/启用{}用户...", userStatusDto.getUsername(), userStatusDto.getDisable());
        userService.disableUser(userStatusDto);
        return WrapMapper.ok();
    }

    /**
     * @Title: resetPassword
     * @Description: 重置密码
     * @param: username
     * @param: password
     * @param: oldPassword
     * @return: Wrapper
     * @author: DnsLin
     * @date: 2024/3/24 16:27
     */
    @SaCheckLogin
    @PostMapping("/reset")
    @LogOperation(description = "重置密码", actionType = ActionType.MODIFY)
    public Wrapper<?> resetPassword(@RequestBody @Validated RestPassDTO restPassDto) {
        AccountInfoDTO account = Common.getLoginAccount(StpUtil.getLoginIdAsLong());
        log.info("用户{}重置密码...", account.getUsername());
        userService.resetPassword(restPassDto,account);
        return WrapMapper.ok();
    }

    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/assign")
    @LogOperation(description = "更新角色", actionType = ActionType.MODIFY)
    public Wrapper<List<String>> addRole(@RequestBody @Validated UserRoleDTO userRoleDto) {
        // 此接口 仅超级管理员可调用
        log.info("管理员为用户{}分配角色", userRoleDto.getUsername());
        return WrapMapper.ok(userService.updateUserRoles(userRoleDto));
    }
}
