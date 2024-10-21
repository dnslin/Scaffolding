package in.dnsl.model.vo;

import in.dnsl.model.info.ErrorInfo;

import java.util.List;

public record UploadResultVo(List<ImageVo> successfulUploads, List<ErrorInfo> failedUploads) {
    public UploadResultVo(ImageVo imageVo, ErrorInfo errorInfo) {
        this(
                imageVo != null ? List.of(imageVo) : List.of(),
                errorInfo != null ? List.of(errorInfo) : List.of()
        );
    }

    public static UploadResultVo success(ImageVo imageVo) {
        return new UploadResultVo(imageVo, null);
    }

    public static UploadResultVo failure(String fileName, String errorMessage) {
        return new UploadResultVo(null, new ErrorInfo(fileName, errorMessage));
    }
}

