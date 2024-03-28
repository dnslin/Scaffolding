package in.dnsl.controller.other;

import lombok.extern.slf4j.Slf4j;
import me.kuku.utils.OkHttpUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Path;

@Slf4j
@RestController
@CrossOrigin
public class TempController {

    @GetMapping("/temp")
    public String temp() {
        OkHttpUtils.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("150.230.121.194", 1080)));
        File file = OkHttpUtils.getFile("https://w.wallhaven.cc/full/we/wallhaven-werowr.png", Path.of("C:\\Users\\dongshilin\\Pictures\\Screenshots"));
        System.out.println(file.getName());
        return "ok";
    }
}
