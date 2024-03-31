package in.dnsl.controller.other;

import in.dnsl.annotation.RateLimitRule;
import in.dnsl.annotation.RateLimiter;
import in.dnsl.enums.LimitType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Controller
@RequestMapping("/error")
public class CustomErrorController {

    @GetMapping("/404")
    @RateLimiter(rules = {@RateLimitRule, @RateLimitRule(time = 10, count = 50)})
    @RateLimiter(rules = {@RateLimitRule(time = 1, count = 2)}, type = LimitType.IP)
    public ResponseEntity<byte[]> notFoundPage() throws Exception {
        return getEntity("static/error/404.html", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/500")
    @RateLimiter(rules = {@RateLimitRule, @RateLimitRule(time = 10, count = 50)})
    @RateLimiter(rules = {@RateLimitRule(time = 1, count = 2)}, type = LimitType.IP)
    public ResponseEntity<byte[]> internalServerErrorPage() throws Exception {
        return getEntity("static/error/500.html", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<byte[]> getEntity(String name, HttpStatus internalServerError) throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(name)).toURI());
        byte[] content = Files.readAllBytes(path);
        return new ResponseEntity<>(content, internalServerError);
    }


}