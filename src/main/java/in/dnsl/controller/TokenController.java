package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.model.dto.GenTokenDto;
import in.dnsl.model.vo.TokenProVo;
import in.dnsl.service.TokenService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/token")
public class TokenController {

    @Resource
    private TokenService tokenService;

    // 生成token
    @SaCheckLogin
    @PostMapping("/generate")
    public Wrapper<TokenProVo> generateToken(@RequestBody @Validated GenTokenDto genTokenDto) {
        log.info("生成token...");
        TokenProVo tokenProVo = tokenService.generateToken(genTokenDto);
        return WrapMapper.ok(tokenProVo);
    }


    // 校验token3
    @SaCheckLogin
    @PostMapping("/checkToken")
    public Wrapper<Boolean> checkToken(@RequestParam String token) {
        log.info("校验token...");
        boolean result = tokenService.checkToken(token);
        return WrapMapper.ok(result);
    }


    // 删除token
    @SaCheckLogin
    @PostMapping("/deleteToken")
    public Wrapper<Void> deleteToken(@RequestParam Long userId, @RequestParam String token) {
        log.info("删除token...");
        tokenService.deleteToken(userId, token);
        return WrapMapper.ok();
    }


    // 删除用户的所有token
    @SaCheckLogin
    @PostMapping("/deleteAllToken")
    public Wrapper<Void> deleteAllToken(@RequestParam Long userId) {
        log.info("删除用户的所有token...");
        tokenService.deleteAllToken(userId);
        return WrapMapper.ok();
    }


    // 删除过期的token
    @SaCheckLogin
    @PostMapping("/deleteExpiredToken")
    public Wrapper<Void> deleteExpiredToken() {
        log.info("删除过期的token...");
        tokenService.deleteExpiredToken();
        return WrapMapper.ok();
    }


    // 获取用户的所有token
    @SaCheckLogin
    @PostMapping("/getTokenList")
    public Wrapper<List<TokenProVo>> getTokenList(@RequestParam Long userId) {
        log.info("获取 用户的所有token...");
        List<TokenProVo> tokenList = tokenService.getTokenList(userId);
        return WrapMapper.ok(tokenList);
    }

    // 延长token有效期
    @SaCheckLogin
    @PostMapping("/extendToken")
    public Wrapper<Void> extendToken(@RequestParam Long userId, @RequestParam String token, @RequestParam Integer days) {
        log.info("延长token有效期...");
        tokenService.extendToken(userId, token, days);
        return WrapMapper.ok();
    }
}
