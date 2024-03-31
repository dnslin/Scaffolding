package in.dnsl.service;

import in.dnsl.model.dto.GenTokenDto;
import in.dnsl.model.vo.TokenProVo;

import java.util.List;

public interface TokenService {
    // 生成token
    TokenProVo generateToken(GenTokenDto genTokenDto);

    // 校验token
    boolean checkToken(String token);

    // 删除token
    void deleteToken(Long userId,String token);

    // 删除用户的所有token
    void deleteAllToken(Long userId);

    // 删除过期的token
    void deleteExpiredToken();

    // 获取 用户的所有token
    List<TokenProVo> getTokenList(Long userId);

    // 延长token有效期
    void extendToken(Long userId, String token,Integer days);
}
