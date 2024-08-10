import in.dnsl.model.domain.WatermarkConfig;
import in.dnsl.utils.PicUtils;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Slf4j
public class WatermarkTest {

    // 定义输入图片路径
    private static final String INPUT_IMAGE_PATH = "C:\\Users\\dnslin\\Downloads\\wallhaven\\test\\";
    private static final String IMAGE_PATH = "C:\\Users\\dnslin\\Downloads\\wallhaven\\";

    public static void main(String[] args) {
        File file = new File("C:\\Users\\dnslin\\Downloads\\wallhaven\\test.jpg");
        try {
            // 加载原始图片
            BufferedImage originalImage = ImageIO.read(file);

            // 测试文本水印
            testTextWatermark(originalImage);

            // 测试图片水印
            testImageWatermark(originalImage);

        } catch (IOException e) {
            log.error("Error reading image file", e);
        }
    }

    private static void testTextWatermark(BufferedImage originalImage) throws IOException {
        WatermarkConfig textConfig = new WatermarkConfig(
                "Copyright 2023",
                null,
                new Font("Arial", Font.BOLD, 40),
                Color.WHITE,
                WatermarkConfig.Position.BOTTOM_RIGHT,
                0.5f,
                1.0f
        );

        BufferedImage textWatermarkedImage = PicUtils.addWatermark(originalImage, textConfig);
        ImageIO.write(textWatermarkedImage, "jpg", new File(INPUT_IMAGE_PATH+"output_text_watermark.jpg"));
        System.out.println("Text watermark added and saved as 'output_text_watermark.jpg'");
    }

    private static void testImageWatermark(BufferedImage originalImage) throws IOException {
        // 加载水印图片
        BufferedImage logoImage = ImageIO.read(new File(IMAGE_PATH+"logo.png"));

        WatermarkConfig imageConfig = new WatermarkConfig(
                null,
                logoImage,
                null,
                null,
                WatermarkConfig.Position.TOP_LEFT,
                0.7f,
                0.5f
        );

        BufferedImage imageWatermarkedImage = PicUtils.addWatermark(originalImage, imageConfig);
        ImageIO.write(imageWatermarkedImage, "jpg", new File(INPUT_IMAGE_PATH+"output_image_watermark.jpg"));
        System.out.println("Image watermark added and saved as 'output_image_watermark.jpg'");
    }
}