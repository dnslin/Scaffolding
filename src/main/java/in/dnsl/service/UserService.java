package in.dnsl.service;

import in.dnsl.model.dto.EditUserDTO;
import in.dnsl.model.dto.LoginDTO;
import in.dnsl.model.vo.UserInfoVo;
import in.dnsl.model.vo.UserVo;

public interface UserService {

    // 创建用户
    void createUser(UserVo info);

    // 登录
    UserInfoVo doLogin(LoginDTO loginDTO);

    // 获取用户信息
    UserInfoVo getUserInfo(String username);

    // 更新用户信息
    void updateUserInfo(EditUserDTO info);

    // 删除用户
    void deleteUser(String username);

    // 禁用用户/启用用户
    void disableUser(String username, boolean disable);

    void resetPassword(String username, String password, String oldPassword);
}
