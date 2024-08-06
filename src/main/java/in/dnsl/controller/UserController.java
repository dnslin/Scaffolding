package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
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
import in.dnsl.utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Wrapper<?> createUser(@Validated @RequestBody UserDto info, HttpServletRequest request) {
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
    public Wrapper<UserInfoVo> doLogin(@Validated @RequestBody LoginDto loginDTO, HttpServletRequest request) {
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
        log.info("用户{}注销...", StpUtil.getLoginIdDefaultNull());
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
    public Wrapper<UserInfoVo> getUserInfo(String username) {
        log.info("{} 查看用户信息...", username);
        return WrapMapper.ok(userService.getUserInfo(username));
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
    public Wrapper<UserInfoVo> updateUserInfo(@Validated @RequestBody EditUserDto info) {
        log.info("用户{}修改用户信息...", info.getUsername());
        return WrapMapper.ok(userService.updateUserInfo(info));
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
    @PostMapping("/delete")
    public Wrapper<?> deleteUser(String username) {
        log.info("用户{}删除用户...", username);
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
    @PostMapping("/disable")
    public Wrapper<?> disableUser(@RequestBody @Validated UserStatusDto userStatusDto) {
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
    public Wrapper<?> resetPassword(@RequestBody @Validated RestPassDto restPassDto) {
        log.info("用户{}重置密码...", restPassDto.getUsername());
        userService.resetPassword(restPassDto);
        return WrapMapper.ok();
    }
}
