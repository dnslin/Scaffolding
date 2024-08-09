package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import in.dnsl.annotation.LogOperation;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.enums.ActionType;
import in.dnsl.model.dto.AccountInfoDto;
import in.dnsl.model.dto.GenTokenDto;
import in.dnsl.model.vo.TokenProVo;
import in.dnsl.service.TokenService;
import in.dnsl.utils.Common;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private final TokenService tokenService;

    // 生成token
    @SaCheckLogin
    @PostMapping("/generate")
    public Wrapper<TokenProVo> generateToken(@RequestBody @Validated GenTokenDto genTokenDto) {
        AccountInfoDto account = Common.getLoginAccount(StpUtil.getLoginIdAsLong());
        TokenProVo tokenProVo = tokenService.generateToken(genTokenDto,account);
        return WrapMapper.ok(tokenProVo);
    }


    // 校验token3
    @SaCheckLogin
    @PostMapping("/checkToken")
    public Wrapper<Boolean> checkToken(@RequestParam String token) {
        boolean result = tokenService.checkToken(token);
        return WrapMapper.ok(result);
    }


    // 删除token
    @SaCheckLogin
    @PostMapping("/deleteToken")
    @LogOperation(description = "删除token", actionType = ActionType.DEL)
    public Wrapper<Void> deleteToken(@RequestParam String token) {
        AccountInfoDto account = Common.getLoginAccount(StpUtil.getLoginIdAsLong());
        tokenService.deleteToken(account.getUserId(), token);
        return WrapMapper.ok();
    }


    // 删除用户的所有token
    @SaCheckLogin
    @PostMapping("/deleteAllToken")
    @LogOperation(description = "删除用户的所有token", actionType = ActionType.DEL)
    public Wrapper<Void> deleteAllToken() {
        AccountInfoDto account = Common.getLoginAccount(StpUtil.getLoginIdAsLong());
        tokenService.deleteAllToken(account.getUserId());
        return WrapMapper.ok();
    }


    // 删除过期的token
    @SaCheckLogin
    @PostMapping("/deleteExpiredToken")
    public Wrapper<Void> deleteExpiredToken() {
        tokenService.deleteExpiredToken();
        return WrapMapper.ok();
    }


    // 获取用户的所有token
    @SaCheckLogin
    @PostMapping("/getTokenList")
    public Wrapper<List<TokenProVo>> getTokenList() {
        AccountInfoDto account = Common.getLoginAccount(StpUtil.getLoginIdAsLong());
        List<TokenProVo> tokenList = tokenService.getTokenList(account.getUserId());
        return WrapMapper.ok(tokenList);
    }

    // 延长token有效期
    @SaCheckLogin
    @PostMapping("/extendToken")
    public Wrapper<Void> extendToken( @RequestParam String token, @RequestParam Integer days) {
        log.info("延长token有效期...");
        AccountInfoDto account = Common.getLoginAccount(StpUtil.getLoginIdAsLong());
        tokenService.extendToken(account.getUserId(), token, days);
        return WrapMapper.ok();
    }
}
