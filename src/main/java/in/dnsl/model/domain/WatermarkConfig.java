package in.dnsl.model.domain;

import java.awt.*;
import java.awt.image.BufferedImage;

public record WatermarkConfig(
        String text,
        BufferedImage image,
        Font font,
        Color color,
        Position position,
        float opacity,
        float scale
) {
    public WatermarkConfig {
        if ((text == null && image == null) || (text != null && image != null)) {
            throw new IllegalArgumentException("必须提供文本或图像，但不能同时提供两者或两者兼而有之.");
        }
        if (text != null && font == null) {
            throw new IllegalArgumentException("必须为文本水印提供字体.");
        }
    }

    public enum Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER
    }
}