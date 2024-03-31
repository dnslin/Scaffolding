package in.dnsl.service;

import in.dnsl.model.entity.Token;

import java.util.List;

public interface TokenService {
    // 生成token
    String generateToken(Integer userId);

    // 校验token
    boolean checkToken(String token);

    // 删除token
    void deleteToken(Integer userId,String token);

    // 删除用户的所有token
    void deleteAllToken(Integer userId);


    // 删除过期的token
    void deleteExpiredToken();

    // 获取 用户的所有token
    List<Token> getTokenList(Integer userId);
}
