package in.dnsl.handler.config;

import in.dnsl.model.domain.WatermarkConfig;

public record ImageConfig(
        int minWidth,
        int minHeight,
        boolean compressionEnabled,
        float compressionQuality,
        boolean formatConversionEnabled,
        String targetFormat,
        boolean watermarkEnabled,
        WatermarkConfig watermarkConfig
) {}