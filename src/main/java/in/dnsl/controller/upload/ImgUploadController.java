package in.dnsl.controller.upload;

import cn.dev33.satoken.annotation.SaCheckLogin;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.model.vo.ImageVo;
import in.dnsl.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class ImgUploadController {

    private final ImageService imageService;

    // 文件上传接口
    @SaCheckLogin
    @PostMapping("/image")
    public Wrapper<ImageVo> uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        File file = new File("path/to/destination/" + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
            return WrapMapper.ok(imageService.uploadImage(file));
        } catch (IOException e) {
            return WrapMapper.error(e.getMessage());
        }
    }
}
