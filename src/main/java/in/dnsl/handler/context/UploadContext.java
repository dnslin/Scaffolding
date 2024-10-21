package in.dnsl.handler.context;


import in.dnsl.handler.config.ImageConfig;

import java.awt.image.BufferedImage;

public record UploadContext(
        BufferedImage image,
        ImageConfig config,
        String fileName,
        String hash
) {
    public UploadContext withImage(BufferedImage newImage) {
        return new UploadContext(newImage, config, fileName, hash);
    }

    public UploadContext withHash(String newHash) {
        return new UploadContext(image, config, fileName, newHash);
    }
}