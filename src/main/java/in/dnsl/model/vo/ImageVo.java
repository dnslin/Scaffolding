package in.dnsl.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ImageVo {

    // 用户ID
    private Integer userId;

    // 域名
    private String domain;

    // 目录
    private String directory;

    // 相册
    private String album;

    // 创建时间
    private Date createDate;

    // 修改时间
    private Date modifyDate;

    // 文件名
    private String fileName;

    // 加密访问链接
    private String encryptedKey;

    // 文件大小
    private Integer size;

    // 图片信息
    private String info;

    // 标签
    private String tags;

    // 可见性
    private Integer visibility;

    // 分类
    private String category;

}
