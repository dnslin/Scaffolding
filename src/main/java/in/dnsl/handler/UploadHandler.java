package in.dnsl.handler;

import in.dnsl.exception.AppException;
import in.dnsl.handler.context.UploadContext;

@FunctionalInterface
public interface UploadHandler {
    boolean handle(UploadContext context) throws AppException;
}