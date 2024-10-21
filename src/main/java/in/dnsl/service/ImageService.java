package in.dnsl.service;

import in.dnsl.model.vo.ImageVo;
import in.dnsl.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final RedisUtils redisUtils;
    // 文件上传接口
    public ImageVo uploadImage(File file) {
        log.info("上传文件: {}", file.getName());
        // 检测文件大小
        // 检测文件类型
        // 检测是否为图片
        return null;
    }


    // 设置密码接口
    // 增加标签接口 或者 删除
    // 切换图片为隐私或者开放
    // 删除图片接口
    // 获取图片接口 根据用户ID
    // 获取图片列表接口 根据用户ID
    // 获取图片标签接口
    // 校验密码接口
    // 更新图片信息 例如文件名 tag 分类或者 隐私状态

}
