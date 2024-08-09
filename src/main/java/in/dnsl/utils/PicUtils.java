package in.dnsl.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import in.dnsl.enums.Position;
import in.dnsl.model.info.ImageInfo;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

// 提供图片的一些工具方法
public class PicUtils {

    /**
     * 添加水印
     *
     * @param originalImage 原始图片
     * @param watermarkText 水印文字
     * @param position      水印位置（如：右下角）
     * @return 添加水印后的图片
     */
    public static BufferedImage addWatermark(BufferedImage originalImage, String watermarkText, Position position) {
        Graphics2D g2d = (Graphics2D) originalImage.getGraphics();
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
        g2d.setComposite(alphaChannel);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 64));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(watermarkText, g2d);

        return getBufferedImage(originalImage, watermarkText, position, g2d, rect);
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


    /**
     * 获取图片格式
     *
     * @param imageFile 图片文件
     * @return 图片格式
     */
    private static String getImageFormat(File imageFile) throws IOException {
        Iterator<ImageReader> readers = ImageIO.getImageReaders(ImageIO.createImageInputStream(imageFile));
        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            return reader.getFormatName();
        }
        return "Unknown";
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
     * 添加文本
     *
     * @param originalImage 原始图片
     * @param text          要添加的文本
     * @param font          字体
     * @param color         颜色
     * @param x             x坐标
     * @param y             y坐标
     * @return 添加文本后的图片
     */
    public static BufferedImage addText(BufferedImage originalImage, String text, Font font, Color color, int x, int y) {
        Graphics2D g2d = originalImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
        g2d.dispose();
        return originalImage;
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


    /**
     * 添加水印图片
     *
     * @param originalImage  原始图片
     * @param watermarkImage 水印图片
     * @param position       水印位置
     * @param opacity        不透明度 (0.0f to 1.0f)
     * @return 添加水印后的图片
     */
    public static BufferedImage addImageWatermark(BufferedImage originalImage, BufferedImage watermarkImage, Position position, float opacity) {
        int x;
        int y = switch (position) {
            case TOP_LEFT -> {
                x = 10;
                yield 10;
            }
            case TOP_RIGHT -> {
                x = originalImage.getWidth() - watermarkImage.getWidth() - 10;
                yield 10;
            }
            case BOTTOM_LEFT -> {
                x = 10;
                yield originalImage.getHeight() - watermarkImage.getHeight() - 10;
            }
            case BOTTOM_RIGHT -> {
                x = originalImage.getWidth() - watermarkImage.getWidth() - 10;
                yield originalImage.getHeight() - watermarkImage.getHeight() - 10;
            }
            case CENTER -> {
                x = (originalImage.getWidth() - watermarkImage.getWidth()) / 2;
                yield (originalImage.getHeight() - watermarkImage.getHeight()) / 2;
            }
        };

        Graphics2D g2d = originalImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(watermarkImage, x, y, null);
        g2d.dispose();

        return originalImage;
    }

    /**
     * 添加文字水印
     *
     * @param originalImage 原始图片
     * @param watermarkText 水印文字
     * @param font          水印字体
     * @param color         水印颜色
     * @param position      水印位置
     * @param opacity       不透明度 (0.0f to 1.0f)
     * @return 添加水印后的图片
     */
    public static BufferedImage addTextWatermark(BufferedImage originalImage, String watermarkText, Font font, Color color, Position position, float opacity) {
        Graphics2D g2d = (Graphics2D) originalImage.getGraphics();

        // 设置合成规则
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        g2d.setFont(font);
        g2d.setColor(color);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(watermarkText, g2d);

        return getBufferedImage(originalImage, watermarkText, position, g2d, rect);
    }

    private static BufferedImage getBufferedImage(BufferedImage originalImage, String watermarkText, Position position, Graphics2D g2d, Rectangle2D rect) {
        int x;
        int y = switch (position) {
            case TOP_LEFT -> {
                x = 10;
                yield (int) rect.getHeight();
            }
            case TOP_RIGHT -> {
                x = originalImage.getWidth() - ((int) rect.getWidth() + 10);
                yield (int) rect.getHeight();
            }
            case BOTTOM_LEFT -> {
                x = 10;
                yield originalImage.getHeight() - 10;
            }
            case BOTTOM_RIGHT -> {
                x = originalImage.getWidth() - ((int) rect.getWidth() + 10);
                yield originalImage.getHeight() - 10;
            }
            case CENTER -> {
                x = (originalImage.getWidth() - (int) rect.getWidth()) / 2;
                yield (originalImage.getHeight() - (int) rect.getHeight()) / 2;
            }
        };

        g2d.drawString(watermarkText, x, y);
        g2d.dispose();

        return originalImage;
    }
}