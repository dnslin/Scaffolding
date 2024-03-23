package in.dnsl.service.impl;

import in.dnsl.constant.Integer4Boolean;
import in.dnsl.exception.AppException;
import in.dnsl.model.entity.User;
import in.dnsl.model.vo.UserVo;
import in.dnsl.repository.UserRepository;
import in.dnsl.service.UserService;
import in.dnsl.utils.PasswordUtils;
import in.dnsl.utils.UniqueIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
