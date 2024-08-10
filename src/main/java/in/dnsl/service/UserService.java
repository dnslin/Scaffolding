package in.dnsl.service;

import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.constant.Integer4Boolean;
import in.dnsl.enums.ActionType;
import in.dnsl.exception.AppException;
import in.dnsl.model.dto.*;
import in.dnsl.model.entity.OperationLog;
import in.dnsl.model.entity.Role;
import in.dnsl.model.entity.User;
import in.dnsl.model.vo.RoleVo;
import in.dnsl.model.vo.UserInfoVo;
import in.dnsl.repository.OperationLogRepository;
import in.dnsl.repository.RoleRepository;
import in.dnsl.repository.UserRepository;
import in.dnsl.utils.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static in.dnsl.enums.ResponseEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    private final OperationLogRepository logRepository;


    private final RoleRepository roleRepository;

    public void createUser(UserDTO info) {
        // 判断用户是否存在
        checkAndGetUser(info.getUsername(), true);
        // 准备ID
        long uniqueId = UniqueIdGenerator.getInstance(Integer4Boolean.APP_HOST).genUniqueId();
        // 对密码进行二次加盐
        String salt = PasswordUtils.getSalt();
        String securePassword = PasswordUtils.generateSecurePassword(info.getPassword(), salt);
        User userInfo = User.builder()
                .id(uniqueId)
                .username(info.getUsername())
                .password(securePassword)
                .avatar(info.getAvatar())
                .email(info.getEmail())
                .salt(salt)
                .nickname(info.getNickname())
                .lastLoginIp(info.getLastLoginIp())
                .lastLoginTime(LocalDateTime.now()).build();
        User save = userRepository.save(userInfo);
        log.info("用户创建成功: {}", save);
    }

    @Transactional(rollbackOn = Exception.class)
    public UserInfoVo doLogin(LoginDTO loginDTO) {
        // 查询用户
        User user = checkAndGetUser(loginDTO.getUsername(), false);
        // 校验密码
        if (PasswordUtils.verifyUserPassword(loginDTO.getPassword(), user.getPassword(), user.getSalt())) {
            log.error("密码错误-->: {}", loginDTO.getUsername());
            throw new AppException(USER_OR_PASSWORD_ERROR);
        }
        // 记录登录日志
        logRepository.save(OperationLog.builder()
                .operationIp(loginDTO.getLastLoginIp())
                .userName(user.getUsername())
                .operationTime(LocalDateTime.now())
                .operationType(ActionType.LOGIN.name())
                .operationDetails("用户登录")
                .ua(loginDTO.getUA())
                .userId(user.getId())
                .build());
        // 更新登录信息
        user.setLastLoginIp(loginDTO.getLastLoginIp());
        user.setLastLoginTime(LocalDateTime.now());
        StpUtil.login(user.getId() + ":" + user.getUsername());
        log.info("用户登录成功: {}", user.getNickname());
        User save = userRepository.save(user);
        UserInfoVo userInfoVo = GenericBeanUtils.copyProperties(save, UserInfoVo.class,false);
        userInfoVo.setTokenInfo(StpUtil.getTokenInfo());
        userInfoVo.setRoles(Optional.ofNullable(user.getRoles())
                .map(roles -> roles.stream()
                        .map(this::convertToRoleVo)
                        .collect(Collectors.toSet()))
                .orElse(null));
        return userInfoVo;
    }

    @Cacheable(value = "PicManager:User:cache:userInfo", key = "#username")
    public UserInfoVo getUserInfo(String username) {
        log.info("查看用户信息-数据量: {}", username);
        User user = checkAndGetUser(username,false);
        UserInfoVo userInfoVo = GenericBeanUtils.copyProperties(user, UserInfoVo.class, false);
        userInfoVo.setRoles(Optional.ofNullable(user.getRoles())
                .map(roles -> roles.stream()
                        .map(this::convertToRoleVo)
                        .collect(Collectors.toSet()))
                .orElse(null));
        return userInfoVo;
    }
    private RoleVo convertToRoleVo(Role role) {
        return new RoleVo(role.getId(), role.getName(), role.getDescription());
    }


    @Transactional(rollbackOn = Exception.class)
    @CachePut(value = "PicManager:User:cache:userInfo", key = "#account.username")
    public UserInfoVo updateUserInfo(EditUserDTO info, AccountInfoDTO account) {
        User user = checkAndGetUser(account.getUsername(),false);
        // 如果为空或者为"" 则不修改
        ReflectionUtils.updateFieldsIfPresent(info, user);
        User save = userRepository.save(user);
        log.info("用户信息修改成功: {}", save);
        return GenericBeanUtils.copyProperties(save, UserInfoVo.class,false);
    }

    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(value = "PicManager:User:cache:userInfo", key = "#username")
    public void deleteUser(String username) {
        User user = checkAndGetUser(username,false);
        userRepository.delete(user);
        log.info("用户删除成功: {}", user);
    }

    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(value = "PicManager:User:cache:userInfo", key = "#userStatusDto.username")
    public void disableUser(UserStatusDTO userStatusDto) {
        User user = checkAndGetUser(userStatusDto.getUsername(),false);
        user.setEnabled(userStatusDto.getDisable());
        User save = userRepository.save(user);
        log.info("用户禁用/启用成功: {}", save);
    }

    public void resetPassword(RestPassDTO restPassDto, AccountInfoDTO account) {
        User user = checkAndGetUser(account.getUsername(),false);
        if (PasswordUtils.verifyUserPassword(restPassDto.getOldPassword(), user.getPassword(), user.getSalt())) {
            log.error("密码错误: {}", account.getUsername());
            throw new AppException(USER_OR_PASSWORD_ERROR);
        }
        String securePassword = PasswordUtils.generateSecurePassword(restPassDto.getPassword(), user.getSalt());
        user.setPassword(securePassword);
        User save = userRepository.save(user);
        log.info("用户密码重置成功: {}", save);
    }

    public Set<Role> getRolesByUserId(Long userId) {
        return userRepository.findByIdAndEnabled(userId,false)
                .map(User::getRoles)
                .orElse(Collections.emptySet());
    }

    // 更新用户角色
    @Transactional(rollbackOn = Exception.class)
    public List<String> updateUserRoles(UserRoleDTO userRoleDto) {
        User user = checkAndGetUser(userRoleDto.getUsername(), false);
        Set<Role> newRoles = userRoleDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new AppException(ROLE_NOT_EXIST)))
                .collect(Collectors.toSet());
        // 清除用户当前的所有角色，然后设置新的角色集合
        user.getRoles().clear();
        user.getRoles().addAll(newRoles);
        User save = userRepository.save(user);
        // 返回用户的角色名称
        return save.getRoles().stream()
                .map(Role::getName)
                .toList();
    }


    // 检查用户是否存在 不存在则抛出异常
    private User checkAndGetUser(String username, boolean checkIfExists) {
        return userRepository.findByUsernameAndEnabled(username,true)
                .or(() -> {
                    if (checkIfExists) throw new AppException(USER_EXIST);
                    return Optional.empty();
                })
                .orElseThrow(() -> new AppException(USER_OR_DISABLE_ERROR));
    }

}
