package in.dnsl.controller.other;

import in.dnsl.model.entity.Wallhaven;
import in.dnsl.repository.WallhavenRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.kuku.utils.OkHttpUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@CrossOrigin
public class TempController {

    @Resource
    private WallhavenRepository repository;

    final String path = "/app/";
    final String baseUrl = "https://w.wallhaven.cc/full/";

    @Transactional
    @GetMapping("/temp")
    public void temp() {
        List<String> typeList = Arrays.asList(".jpg", ".png");
        OkHttpUtils.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("150.230.121.194", 1080)));
        List<Wallhaven> wallhavenByStatus = repository.findWallhavenByStatus(0);
        log.info("待下载数量: {}", wallhavenByStatus.size());
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            wallhavenByStatus.forEach(item ->
                    executorService.submit(() -> typeList.forEach(type -> {
                        String fullUrl = generateFullUrl(item, type);
                        log.info("下载类型为{}下载地址: {}", type, fullUrl);
                        downloadFile(fullUrl, type, item.getId());
                    }))
            );
        }
    }

    private String generateFullUrl(Wallhaven inputItem, String type) {
        String url = inputItem.getUrl();
        String smallUrl = url.substring(url.lastIndexOf('/') + 1);
        String littleUrl = smallUrl.substring(0, 2);
        return baseUrl + littleUrl + "/wallhaven-" + smallUrl + type;
    }

    protected void downloadFile(String url, String type, Integer id) {
        File file = new File(path + id + type);
        try {
            file = OkHttpUtils.getFile(url, file);
            log.info("文件大小: {}", file.length());
            if (file.length() < 2048) throw new Exception("文件大小异常");
            repository.deleteById(id);
            log.info("数据库删除成功: {}", id);
        } catch (Exception e) {
            boolean delete = file.delete();
            log.info("删除文件状态: {}", delete);
            log.error("下载失败: {}", e.getMessage());
        }
    }
}
