package in.dnsl.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/info")
public class InfoController {

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
