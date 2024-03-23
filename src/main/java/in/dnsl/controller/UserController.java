package in.dnsl.controller;

import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    /**
     * @Title: createUser
     * @Description: 创建用户
     * @param: []
     * @return: Wrapper
     * @author: DnsLin
     * @date: 2024/3/23 14:47
     */
    @PostMapping("/create")
    public Wrapper<?> createUser(User info) {
        log.info("创建用户");
        return WrapMapper.ok();
    }
}
