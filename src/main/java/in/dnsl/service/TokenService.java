package in.dnsl.service;

import in.dnsl.constant.RedisKeyConstant;
import in.dnsl.exception.AppException;
import in.dnsl.model.dto.AccountInfoDTO;
import in.dnsl.model.dto.GenTokenDTO;
import in.dnsl.model.entity.Token;
import in.dnsl.model.entity.User;
import in.dnsl.model.vo.TokenProVo;
import in.dnsl.repository.TokenRepository;
import in.dnsl.repository.UserRepository;
import in.dnsl.utils.JSON;
import in.dnsl.utils.RedisUtils;
import in.dnsl.utils.TokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static in.dnsl.enums.ResponseEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;

    private final TokenRepository repository;

    private final RedisUtils redisUtils;


    @Transactional(rollbackOn = Exception.class)
    @Cacheable(value = "PicManager:Token:cache:token" ,key = "#account.userId")
    public TokenProVo generateToken(GenTokenDTO dto, AccountInfoDTO account) {
        User user = userRepository.findByIdAndEnabled(account.getUserId(), true)
                .orElseThrow(() -> new AppException(USER_NOT_EXIST));
        // 判断tokenName是否存在 如果存在则抛出异常
        repository.findByUserIdAndName(account.getUserId(), dto.getTokenName())
                .ifPresent(token -> {
                    throw new AppException(TOKEN_NAME);
                });
        // 生成Token
        String tokenValue = TokenGenerator.generateToken();
        // 保存Token
        Token token = Token.builder()
                .userId(user.getId())
                .name(dto.getTokenName())
                .token(String.valueOf(tokenValue))
                .creationTime(LocalDateTime.now())
                .expirationTime(LocalDateTime.now().plusDays(dto.getDay()))
                .build();
        Token save = repository.save(token);
        // 往 redis 存放一份 token
        String key = String.format(RedisKeyConstant.TOKEN_KEY, account.getUserId(), save.getTokenId());
        String value = JSON.toJSON(save);
        redisUtils.setEx(key, value, dto.getDay(), TimeUnit.DAYS);
        log.info("用户{}生成token:{}", user.getUsername(), save.getToken());
        return TokenProVo.builder()
                .token(save.getToken())
                .name(dto.getTokenName())
                .creationTime(save.getCreationTime())
                .expirationTime(save.getExpirationTime())
                .build();
    }

    public boolean checkToken(String token) {
        // 先根据token查出tokenId和用户id,不存在则过期或者不存在
        Token tokenEntity = repository.findByToken(token)
                .orElseThrow(() -> new AppException(TOKEN_NOT));
        // 拼接redis key
        String key = String.format(RedisKeyConstant.TOKEN_KEY, tokenEntity.getUserId(), tokenEntity.getTokenId());
        // 在redis里面查找token 为 null 则过期或者不存在
        String tokenValue = redisUtils.get(key);
        if (tokenValue == null) throw new AppException(TOKEN_NOT);
        return true;
    }

    @Transactional(rollbackOn = Exception.class)
    @Cacheable(value = "PicManager:Token:cache:list" ,key = "#userId")
    public void deleteToken(Long userId, String token) {
        Token tokenEntity = repository.findByUserIdAndToken(userId, token)
                .orElseThrow(() -> new AppException(TOKEN_NOT));
        repository.delete(tokenEntity);
        // 删除redis里面的token 构建redis key
        String key = String.format(RedisKeyConstant.TOKEN_KEY, userId, tokenEntity.getTokenId());
        redisUtils.delete(key);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteAllToken(Long userId) {
        Integer count = repository.deleteByUserId(userId);
        log.info("用户{}删除了{}个token", userId, count);
        // 删除redis里面的token
        String key = String.format(RedisKeyConstant.TOKEN_KEY, userId, "*");
        redisUtils.keys(key).forEach(redisUtils::delete);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteExpiredToken() {
        List<Token> tokens = repository.findAll();
        tokens.forEach(token -> {
            if (token.getExpirationTime().isBefore(LocalDateTime.now())) repository.delete(token);
        });
    }

    @Cacheable(value = "PicManager:Token:cache:token-list" ,key = "#userId")
    public List<TokenProVo> getTokenList(Long userId) {
        boolean userCheck = userRepository.existsById(userId);
        if (!userCheck) throw new AppException(USER_NOT_EXIST);
        // 从redis里面获取用户的所有token
        String key = String.format(RedisKeyConstant.TOKEN_KEY, userId, "*");
        Set<String> keys = redisUtils.keys(key);
        if (keys.isEmpty()) return new ArrayList<>();
        // 从redis里面获取所有的token
        List<TokenProVo> tokenProVos = new ArrayList<>();
        keys.forEach(redisKey -> {
            String json = redisUtils.get(redisKey);
            tokenProVos.add(JSON.parse(json, TokenProVo.class));
        });
        log.info("用户-->{}<--获取了{}个token", userId, tokenProVos.size());
        return tokenProVos;
    }

    public void extendToken(Long userId, String token, Integer days) {
        Token tokenEntity = repository.findByUserIdAndToken(userId, token)
                .orElseThrow(() -> new AppException(TOKEN_NOT));
        // 延长token有效期
        tokenEntity.setExpirationTime(tokenEntity.getExpirationTime().plusDays(days));
        repository.save(tokenEntity);
        // 更新redis里面的token 获取原来的有效期 + 新增的有效期
        String key = String.format(RedisKeyConstant.TOKEN_KEY, userId, tokenEntity.getTokenId());
        String value = JSON.toJSON(tokenEntity);
        // 获取原来的有效期 + 新增的有效期
        Long expire = redisUtils.getExpire(key, TimeUnit.DAYS);
        redisUtils.setEx(key, value, expire + days, TimeUnit.DAYS);
    }
}
