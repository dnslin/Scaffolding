package in.dnsl.handler.result;

import java.util.List;

public record UploadResult(
        List<SuccessfulUpload> successfulUploads,
        List<FailedUpload> failedUploads
) {
    public record SuccessfulUpload(String originalFilename, String url) {}
    public record FailedUpload(String originalFilename, String errorMessage) {}
}