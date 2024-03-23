package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.model.vo.UserVo;
import in.dnsl.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * @Title: createUser
     * @Description: 创建用户
     * @param: []
     * @return: Wrapper
     * @author: DnsLin
     * @date: 2024/3/23 14:47
     */
    @SaIgnore
    @PostMapping("/create")
    public Wrapper<?> createUser(@Validated @RequestBody UserVo info) {
        log.info("创建用户...");
        userService.createUser(info);
        return WrapMapper.ok();
    }
}
