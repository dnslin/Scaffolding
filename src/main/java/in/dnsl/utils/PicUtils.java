package in.dnsl.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import in.dnsl.exception.AppException;
import in.dnsl.model.domain.WatermarkConfig;
import in.dnsl.model.info.ImageInfo;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static in.dnsl.enums.ResponseEnum.IMG_FORMAT_CHECK_ERROR;

// 提供图片的一些工具方法
@Slf4j
public class PicUtils {

    private static final Map<String, String> MAGIC_NUMBERS = new HashMap<>();

    static {
        MAGIC_NUMBERS.put("89504E47", "png");
        MAGIC_NUMBERS.put("FFD8FF", "jpg");
        MAGIC_NUMBERS.put("47494638", "gif");
        MAGIC_NUMBERS.put("52494646", "webp");
        // 可以根据需要添加更多格式 这里考虑数据库存储通过Redis缓存进获取
    }


    /**
     * 压缩图片
     *
     * @param originalImage 原始图片
     * @param quality       压缩质量（0-1）
     * @return 压缩后的图片字节数组
     */
    public static byte[] compressImage(BufferedImage originalImage, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .scale(1f)
                .outputQuality(quality)
                .outputFormat("jpg")
                .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }


    /**
     * 转换图片格式
     *
     * @param originalImage 原始图片
     * @param formatName    目标格式（如："png", "jpg", "gif", "webp"）
     * @return 转换格式后的图片字节数组
     */
    public static byte[] convertFormat(BufferedImage originalImage, String formatName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if ("webp".equalsIgnoreCase(formatName)) {
            // 使用 WebP 编码器
            ImageIO.write(originalImage, "webp", outputStream);
        } else {
            // 对于其他格式，使用标准的 ImageIO
            ImageIO.write(originalImage, formatName, outputStream);
        }

        return outputStream.toByteArray();
    }


    /**
     * 获取图片信息
     *
     * @param imageFile 图片文件
     * @return 图片信息字符串
     */
    public static ImageInfo getDetailedImageInfo(File imageFile) throws Exception {
        ImageInfo imageInfo = new ImageInfo();

        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

        for (Directory directory : metadata.getDirectories()) {
            String directoryName = directory.getName();
            for (Tag tag : directory.getTags()) {
                imageInfo.addTag(directoryName, tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                directory.getErrors().forEach(imageInfo::addError);
            }
        }

        return imageInfo;
    }

    public static CompletableFuture<String> validateImageAsync(File imageFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] fileContent = Files.readAllBytes(imageFile.toPath());

                CompletableFuture<String> extensionFuture = CompletableFuture.supplyAsync(
                        () -> getFileExtension(imageFile.getName()));

                CompletableFuture<String> imageIoFuture = CompletableFuture.supplyAsync(
                        () -> getImageFormatUsingImageIO(fileContent, imageFile.getName()));

                CompletableFuture<String> magicNumberFuture = CompletableFuture.supplyAsync(
                        () -> getImageFormatUsingMagicNumber(fileContent, imageFile.getName()));

                return CompletableFuture.allOf(extensionFuture, imageIoFuture, magicNumberFuture)
                        .thenApply(v -> {
                            String extension = extensionFuture.join();
                            String imageIoFormat = imageIoFuture.join();
                            String magicNumberFormat = magicNumberFuture.join();

                            if (extension.equals(imageIoFormat) && extension.equals(magicNumberFormat)) {
                                return extension;
                            } else {
                                log.error("文件格式不一致: 文件扩展名 = {}, ImageIO = {}, 魔数 = {}", extension, imageIoFormat, magicNumberFormat);
                                throw new AppException(IMG_FORMAT_CHECK_ERROR);
                            }
                        }).join();
            } catch (IOException e) {
                log.error("读取文件时出错:{} ", imageFile.getName(), e);
                return "unknown";
            }
        }, Executors.newVirtualThreadPerTaskExecutor());
    }

    // 获取文件扩展名
    private static String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1).toLowerCase();
    }

    // 使用 ImageIO 获取图片格式
    private static String getImageFormatUsingImageIO(byte[] fileContent, String name) {
        try (var iis = ImageIO.createImageInputStream(new ByteArrayInputStream(fileContent))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                return reader.getFormatName().toLowerCase();
            }
        } catch (IOException e) {
            log.error("无法使用 ImageIO 获取图片格式: {}", name);
        }
        return "unknown";
    }

    // 使用魔数检测图片格式
    private static String getImageFormatUsingMagicNumber(byte[] fileContent, String name) {
        String hexString = bytesToHex(fileContent, 8); // 只需要前8个字节

        for (Map.Entry<String, String> entry : MAGIC_NUMBERS.entrySet()) {
            if (hexString.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        log.error("无法使用魔数检测图片格式: {}", name);
        return "unknown";
    }

    private static String bytesToHex(byte[] bytes, int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Math.min(bytes.length, length); i++) {
            result.append(String.format("%02X", bytes[i]));
        }
        return result.toString();
    }

    /**
     * 调整图片大小
     *
     * @param originalImage 原始图片
     * @param width         目标宽度
     * @param height        目标高度
     * @return 调整大小后的图片
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) throws IOException {
        return Thumbnails.of(originalImage)
                .size(width, height)
                .asBufferedImage();
    }

    /**
     * 旋转图片
     *
     * @param originalImage 原始图片
     * @param degrees       旋转角度
     * @return 旋转后的图片
     */
    public static BufferedImage rotateImage(BufferedImage originalImage, double degrees) throws IOException {
        return Thumbnails.of(originalImage)
                .rotate(degrees)
                .asBufferedImage();
    }


    /**
     * 剪裁图片
     *
     * @param originalImage 原始图片
     * @param x             起始 x 坐标
     * @param y             起始 y 坐标
     * @param width         剪裁宽度
     * @param height        剪裁高度
     * @return 剪裁后的图片
     */
    public static BufferedImage cropImage(BufferedImage originalImage, int x, int y, int width, int height) {
        return originalImage.getSubimage(x, y, width, height);
    }


    /**
     * 添加边框
     *
     * @param originalImage 原始图片
     * @param borderWidth   边框宽度
     * @param borderColor   边框颜色
     * @return 添加边框后的图片
     */
    public static BufferedImage addBorder(BufferedImage originalImage, int borderWidth, int borderHigh, Color borderColor) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage borderedImage = new BufferedImage(width + 2 * borderWidth, height + 2 * borderWidth, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = borderedImage.createGraphics();
        g2d.setColor(borderColor);
        g2d.fillRect(0, 0, width + 2 * borderWidth, height + 2 * borderHigh);
        g2d.drawImage(originalImage, borderWidth, borderHigh, null);
        g2d.dispose();

        return borderedImage;
    }


    /**
     * 应用灰度滤镜
     *
     * @param originalImage 原始图片
     * @return 灰度处理后的图片
     */
    public static BufferedImage applyGrayscaleFilter(BufferedImage originalImage) {
        BufferedImage grayscaleImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayscaleImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();
        return grayscaleImage;
    }


    /**
     * 调整图片亮度
     *
     * @param originalImage 原始图片
     * @param factor        亮度因子 (0.0 到 2.0, 1.0 为原始亮度)
     * @return 调整亮度后的图片
     */
    public static BufferedImage adjustBrightness(BufferedImage originalImage, float factor) {
        RescaleOp op = new RescaleOp(factor, 0, null);
        return op.filter(originalImage, null);
    }


    /**
     * 图片拼接
     *
     * @param image1       第一张图片
     * @param image2       第二张图片
     * @param isHorizontal 是否水平拼接
     * @return 拼接后的图片
     */
    public static BufferedImage joinImages(BufferedImage image1, BufferedImage image2, boolean isHorizontal) {
        int width = isHorizontal ? image1.getWidth() + image2.getWidth() : Math.max(image1.getWidth(), image2.getWidth());
        int height = isHorizontal ? Math.max(image1.getHeight(), image2.getHeight()) : image1.getHeight() + image2.getHeight();
        BufferedImage joinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = joinedImage.createGraphics();
        g2d.drawImage(image1, 0, 0, null);
        g2d.drawImage(image2, isHorizontal ? image1.getWidth() : 0, isHorizontal ? 0 : image1.getHeight(), null);
        g2d.dispose();

        return joinedImage;
    }


    /**
     * 添加圆角
     *
     * @param originalImage 原始图片
     * @param cornerRadius  圆角半径
     * @return 添加圆角后的图片
     */
    public static BufferedImage addRoundCorners(BufferedImage originalImage, int cornerRadius) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(originalImage, 0, 0, null);
        g2.dispose();

        return roundedImage;
    }


    /**
     * 生成缩略图
     *
     * @param originalImage 原始图片
     * @param width         缩略图宽度
     * @param height        缩略图高度
     * @return 生成的缩略图
     */
    public static BufferedImage generateThumbnail(BufferedImage originalImage, int width, int height) throws IOException {
        return Thumbnails.of(originalImage)
                .size(width, height)
                .crop(Positions.CENTER)
                .asBufferedImage();
    }

    private static BufferedImage addTextWatermark(BufferedImage originalImage, String text, Font font, Color color,
                                                  WatermarkConfig.Position position, float opacity, float scale) {
        Graphics2D g2d = originalImage.createGraphics();
        configureGraphics(g2d, font, color, opacity);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);
        Point p = calculatePosition(originalImage, (int) (rect.getWidth() * scale), (int) (rect.getHeight() * scale), position);

        AffineTransform at = new AffineTransform();
        at.translate(p.x, p.y);
        at.scale(scale, scale);
        g2d.setTransform(at);

        g2d.drawString(text, 0, (int) rect.getHeight());
        g2d.dispose();
        return originalImage;
    }

    private static BufferedImage addImageWatermark(BufferedImage originalImage, BufferedImage watermarkImage,
                                                   Color color, WatermarkConfig.Position position, float opacity, float scale) {
        int width = (int) (watermarkImage.getWidth() * scale);
        int height = (int) (watermarkImage.getHeight() * scale);
        Point p = calculatePosition(originalImage, width, height, position);

        Graphics2D g2d = originalImage.createGraphics();
        configureGraphics(g2d, null, color, opacity);

        g2d.drawImage(watermarkImage, p.x, p.y, width, height, null);
        g2d.dispose();
        return originalImage;
    }

    private static void configureGraphics(Graphics2D g2d, Font font, Color color, float opacity) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if (font != null) g2d.setFont(font);
        if (color != null) g2d.setColor(color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private static Point calculatePosition(BufferedImage image, int width, int height, WatermarkConfig.Position position) {
        return switch (position) {
            case TOP_LEFT -> new Point(10, 10);
            case TOP_RIGHT -> new Point(image.getWidth() - width - 10, 10);
            case BOTTOM_LEFT -> new Point(10, image.getHeight() - height - 10);
            case BOTTOM_RIGHT -> new Point(image.getWidth() - width - 10, image.getHeight() - height - 10);
            case CENTER -> new Point((image.getWidth() - width) / 2, (image.getHeight() - height) / 2);
        };
    }


    public static BufferedImage addWatermark(BufferedImage originalImage, WatermarkConfig config) {
        if (config.text() != null && config.image() == null) {
            return addTextWatermark(originalImage, config.text(), config.font(), config.color(),
                    config.position(), config.opacity(), config.scale());
        } else if (config.text() == null && config.image() != null) {
            return addImageWatermark(originalImage, config.image(), config.color(),
                    config.position(), config.opacity(), config.scale());
        } else {
            throw new IllegalArgumentException("水印配置无效: 必须提供文本或图像，但不能同时提供两者或两者兼而有之.");
        }
    }

    public static String calculateImageHash(BufferedImage image,String type) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, type, outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(imageBytes);

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("计算图像哈希时出错", e);
        }
    }
}