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
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/version")
    public String version() {
        return "1.0.0";
    }

    @GetMapping("/author")
    public String author() {
        return "DnsLin";
    }

    @GetMapping("/description")
    public String description() {
        return "This is a simple Spring Boot application.";
    }

    @GetMapping("/contact")
    public String contact() {
        return "";

    }
}
