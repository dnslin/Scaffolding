package in.dnsl.service.impl;

import in.dnsl.model.entity.Token;
import in.dnsl.service.TokenService;
import in.dnsl.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisUtils redisUtils;


    @Override
    public String generateToken(Integer userId) {
        // pan
        return null;
    }

    @Override
    public boolean checkToken(String token) {
        return false;
    }

    @Override
    public void deleteToken(Integer userId, String token) {

    }

    @Override
    public void deleteAllToken(Integer userId) {

    }

    @Override
    public void deleteExpiredToken() {

    }

    @Override
    public List<Token> getTokenList(Integer userId) {
        return null;
    }
}
