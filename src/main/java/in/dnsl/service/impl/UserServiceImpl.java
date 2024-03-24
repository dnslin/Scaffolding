package in.dnsl.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.constant.Integer4Boolean;
import in.dnsl.exception.AppException;
import in.dnsl.model.dto.EditUserDTO;
import in.dnsl.model.dto.LoginDTO;
import in.dnsl.model.entity.User;
import in.dnsl.model.vo.UserInfoVo;
import in.dnsl.model.vo.UserVo;
import in.dnsl.repository.UserRepository;
import in.dnsl.service.UserService;
import in.dnsl.utils.PasswordUtils;
import in.dnsl.utils.ReflectionUtils;
import in.dnsl.utils.UniqueIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    @Override
    public void createUser(UserVo info) {
        // 判断用户是否存在
        userRepository.findByUsername(info.getUsername()).ifPresent(user -> {
            log.error("用户已存在: {}", info.getUsername());
            throw new AppException("用户已存在");
        });
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
                .lastLoginTime(LocalDateTime.now())
                .build();
        User save = userRepository.save(userInfo);
        log.info("用户创建成功: {}", save);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserInfoVo doLogin(LoginDTO loginDTO) {
        // 查询用户
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new AppException("用户不存在"));
        // 校验密码
        if (!PasswordUtils.verifyUserPassword(loginDTO.getPassword(), user.getPassword(), user.getSalt())) {
            log.error("密码错误: {}", loginDTO.getUsername());
            throw new AppException("用户或者密码错误");
        }
        // 更新登录信息
        user.setLastLoginIp(loginDTO.getLastLoginIp());
        user.setLastLoginTime(LocalDateTime.now());
        StpUtil.login(user.getId());
        log.info("用户登录成功: {}", user.getNickname());
        User save = userRepository.save(user);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(save, userInfoVo);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        userInfoVo.setTokenInfo(tokenInfo);
        return userInfoVo;
    }

    @Override
    public UserInfoVo getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("用户不存在"));
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        return userInfoVo;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateUserInfo(EditUserDTO info) {
        User user = userRepository.findByUsername(info.getUsername())
                .orElseThrow(() -> new AppException("用户不存在"));
        // 如果为空或者为"" 则不修改
        ReflectionUtils.updateFieldsIfPresent(info, user);
        User save = userRepository.save(user);
        log.info("用户信息修改成功: {}", save);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("用户不存在"));
        userRepository.delete(user);
        log.info("用户删除成功: {}", user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void disableUser(String username,boolean disable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("用户不存在"));
        user.setEnabled(disable);
        User save = userRepository.save(user);
        log.info("用户禁用/启用成功: {}", save);
    }

    @Override
    public void resetPassword(String username, String password, String oldPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("用户不存在"));
        if (!PasswordUtils.verifyUserPassword(oldPassword, user.getPassword(), user.getSalt())) {
            log.error("密码错误: {}", username);
            throw new AppException("密码错误");
        }
        String salt = PasswordUtils.getSalt();
        String securePassword = PasswordUtils.generateSecurePassword(password, salt);
        user.setPassword(securePassword);
        User save = userRepository.save(user);
        log.info("用户密码重置成功: {}", save);
    }

}
