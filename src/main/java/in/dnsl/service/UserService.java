package in.dnsl.service;

import in.dnsl.model.dto.*;
import in.dnsl.model.vo.UserInfoVo;

public interface UserService {

    // 创建用户
    void createUser(UserDto info);

    // 登录
    UserInfoVo doLogin(LoginDto loginDTO);

    // 获取用户信息
    UserInfoVo getUserInfo(String username);

    // 更新用户信息
    void updateUserInfo(EditUserDto info);

    // 删除用户
    void deleteUser(String username);

    // 禁用用户/启用用户
    void disableUser(UserStatusDto userStatusDto);

    // 重置密码
    void resetPassword(RestPassDto restPassDto);
}
