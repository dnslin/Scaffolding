import in.dnsl.enums.Position;
import in.dnsl.model.info.ImageInfo;
import in.dnsl.utils.JSON;
import in.dnsl.utils.PicUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class PicUtilsTest {
    private BufferedImage testImage;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // 创建一个测试用的图片
        testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = testImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
    }

    @Test
    void testAddWatermark() {
        BufferedImage result = PicUtils.addWatermark(testImage, "Test",
                Position.CENTER);
        log.info("result:");
        assertNotNull(result);
        // 这里可以添加更多的断言来检查水印是否正确添加
    }

    @Test
    void testCompressImage() throws IOException {
        byte[] compressed = PicUtils.compressImage(testImage, 0.5f);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    @Test
    void testConvertFormat() throws IOException {
        byte[] converted = PicUtils.convertFormat(testImage, "png");
        assertNotNull(converted);
        assertTrue(converted.length > 0);
    }

    @Test
    void testGetImageInfo() throws Exception {
        File imageFile = new File(tempDir.toFile(), "test.jpg");
        ImageIO.write(testImage, "jpg", imageFile);

        ImageInfo info = PicUtils.getDetailedImageInfo(imageFile);
        assertNotNull(info);
        log.info("info:{}", JSON.toJSON(info));
    }

    @Test
    void testResizeImage() throws IOException {
        BufferedImage resized = PicUtils.resizeImage(testImage, 50, 50);
        assertEquals(50, resized.getWidth());
        assertEquals(50, resized.getHeight());
    }

    @Test
    void testRotateImage() throws IOException {
        BufferedImage rotated = PicUtils.rotateImage(testImage, 90);
        assertNotNull(rotated);
        // 可以添加更多断言来检查旋转是否正确
    }

    @Test
    void testCropImage() {
        BufferedImage cropped = PicUtils.cropImage(testImage, 25, 25, 50, 50);
        assertEquals(50, cropped.getWidth());
        assertEquals(50, cropped.getHeight());
    }

    @Test
    void testAddBorder() {
        BufferedImage bordered = PicUtils.addBorder(testImage, 5, 5, Color.BLACK);
        assertEquals(110, bordered.getWidth());
        assertEquals(110, bordered.getHeight());
    }

    @Test
    void testApplyGrayscaleFilter() {
        BufferedImage grayscale = PicUtils.applyGrayscaleFilter(testImage);
        assertNotNull(grayscale);
        assertEquals(BufferedImage.TYPE_BYTE_GRAY, grayscale.getType());
    }

    @Test
    void testAdjustBrightness() {
        BufferedImage brightened = PicUtils.adjustBrightness(testImage, 1.5f);
        assertNotNull(brightened);
        // 可以添加更多断言来检查亮度是否正确调整
    }

    @Test
    void testJoinImages() {
        BufferedImage joined = PicUtils.joinImages(testImage, testImage, true);
        assertEquals(200, joined.getWidth());
        assertEquals(100, joined.getHeight());
    }

    @Test
    void testAddRoundCorners() {
        BufferedImage rounded = PicUtils.addRoundCorners(testImage, 10);
        assertNotNull(rounded);
        // 可以添加更多断言来检查圆角是否正确添加
    }

    @Test
    void testAddText() {
        BufferedImage withText = PicUtils.addText(testImage, "Test", new Font("Arial", Font.PLAIN, 12), Color.BLACK, 10, 20);
        assertNotNull(withText);
        // 可以添加更多断言来检查文本是否正确添加
    }

    @Test
    void testGenerateThumbnail() throws IOException {
        BufferedImage thumbnail = PicUtils.generateThumbnail(testImage, 50, 50);
        assertEquals(50, thumbnail.getWidth());
        assertEquals(50, thumbnail.getHeight());
    }

    @Test
    void testAddImageWatermark() {
        BufferedImage watermark = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        BufferedImage withWatermark = PicUtils.addImageWatermark(testImage, watermark, Position.BOTTOM_RIGHT, 0.5f);
        assertNotNull(withWatermark);
        // 可以添加更多断言来检查水印是否正确添加
    }

}
