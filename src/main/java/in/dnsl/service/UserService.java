package in.dnsl.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.constant.Integer4Boolean;
import in.dnsl.enums.ActionType;
import in.dnsl.exception.AppException;
import in.dnsl.model.dto.*;
import in.dnsl.model.entity.OperationLog;
import in.dnsl.model.entity.Role;
import in.dnsl.model.entity.User;
import in.dnsl.model.vo.UserInfoVo;
import in.dnsl.repository.OperationLogRepository;
import in.dnsl.repository.UserRepository;
import in.dnsl.utils.GenericBeanUtils;
import in.dnsl.utils.PasswordUtils;
import in.dnsl.utils.ReflectionUtils;
import in.dnsl.utils.UniqueIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    private final OperationLogRepository logRepository;

    public void createUser(UserDto info) {
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
    public UserInfoVo doLogin(LoginDto loginDTO) {
        // 查询用户
        User user = checkAndGetUser(loginDTO.getUsername(), false);
        // 校验密码
        if (PasswordUtils.verifyUserPassword(loginDTO.getPassword(), user.getPassword(), user.getSalt())) {
            log.error("密码错误-->: {}", loginDTO.getUsername());
            throw new AppException("用户或者密码错误");
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
        UserInfoVo userInfoVo = GenericBeanUtils.copyProperties(save, UserInfoVo.class);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        userInfoVo.setTokenInfo(tokenInfo);
        return userInfoVo;
    }

    @Cacheable(value = "PicManager:User:cache:userInfo", key = "#username")
    public UserInfoVo getUserInfo(String username) {
        log.info("查看用户信息-数据量: {}", username);
        User user = checkAndGetUser(username,false);
        return GenericBeanUtils.copyProperties(user, UserInfoVo.class);
    }


    @Transactional(rollbackOn = Exception.class)
    @CachePut(value = "PicManager:User:cache:userInfo", key = "#info.username")
    public UserInfoVo updateUserInfo(EditUserDto info) {
        User user = checkAndGetUser(info.getUsername(),false);
        // 如果为空或者为"" 则不修改
        ReflectionUtils.updateFieldsIfPresent(info, user);
        User save = userRepository.save(user);
        log.info("用户信息修改成功: {}", save);
        return GenericBeanUtils.copyProperties(save, UserInfoVo.class);
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
    public void disableUser(UserStatusDto userStatusDto) {
        User user = checkAndGetUser(userStatusDto.getUsername(),false);
        user.setEnabled(userStatusDto.getDisable());
        User save = userRepository.save(user);
        log.info("用户禁用/启用成功: {}", save);
    }

    public void resetPassword(RestPassDto restPassDto) {
        User user = checkAndGetUser(restPassDto.getUsername(),false);
        if (PasswordUtils.verifyUserPassword(restPassDto.getOldPassword(), user.getPassword(), user.getSalt())) {
            log.error("密码错误: {}", restPassDto.getUsername());
            throw new AppException("密码错误");
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



    private User checkAndGetUser(String username, boolean checkIfExists) {
        return userRepository.findByUsernameAndEnabled(username,true)
                .or(() -> {
                    if (checkIfExists) throw new AppException("用户已存在");
                    return Optional.empty();
                })
                .orElseThrow(() -> new AppException("用户不存在,或者已被禁用"));
    }

}
