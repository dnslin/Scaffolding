package in.dnsl.controller.other;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Controller
@RequestMapping("/error")
public class CustomErrorController {

    @GetMapping("/404")
    public ResponseEntity<byte[]> notFoundPage() throws Exception {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("static/error/404.html")).toURI());
        byte[] content = Files.readAllBytes(path);
        return new ResponseEntity<>(content, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/500")
    public ResponseEntity<byte[]> internalServerErrorPage() throws Exception {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("static/error/500.html")).toURI());
        byte[] content = Files.readAllBytes(path);
        return new ResponseEntity<>(content, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}