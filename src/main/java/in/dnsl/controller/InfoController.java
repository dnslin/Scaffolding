package in.dnsl.controller;

import in.dnsl.annotation.RateLimitRule;
import in.dnsl.annotation.RateLimiter;
import in.dnsl.enums.LimitType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/info")
public class InfoController {
    @RateLimiter(rules = {@RateLimitRule, @RateLimitRule(time = 10, count = 50)})
    @RateLimiter(rules = {@RateLimitRule(time = 1, count = 2)}, type = LimitType.IP)
    @GetMapping("/version")
    public String version() {
        return "0.5";
    }

    @GetMapping("/author")
    public String author() {
        return "DnsLin";
    }

    @GetMapping("/description")
    public String description() {
        return "这是一个个人使用的图床项目，用于存储图片并提供外链服务。";
    }
}
