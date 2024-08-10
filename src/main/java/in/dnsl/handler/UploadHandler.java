package in.dnsl.handler;

import in.dnsl.exception.AppException;
import in.dnsl.model.context.UploadContext;

@FunctionalInterface
public interface UploadHandler {
    boolean handle(UploadContext context) throws AppException;
}