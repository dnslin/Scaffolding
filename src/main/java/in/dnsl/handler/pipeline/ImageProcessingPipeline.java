package in.dnsl.handler.pipeline;


import in.dnsl.exception.AppException;
import in.dnsl.handler.UploadHandler;
import in.dnsl.handler.context.UploadContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ImageProcessingPipeline {
    private final List<UploadHandler> mandatoryHandlers;
    private final List<UploadHandler> optionalHandlers;

    public ImageProcessingPipeline(List<UploadHandler> mandatoryHandlers, List<UploadHandler> optionalHandlers) {
        this.mandatoryHandlers = mandatoryHandlers;
        this.optionalHandlers = optionalHandlers;
    }

    public CompletableFuture<UploadContext> process(UploadContext initialContext) {
        return CompletableFuture.supplyAsync(() -> {

            // 执行强制处理步骤
            for (UploadHandler handler : mandatoryHandlers) {
                if (!handler.handle(initialContext)) {
                    throw new AppException("Mandatory processing step failed");
                }
            }

            // 执行可选处理步骤
            for (UploadHandler handler : optionalHandlers) {
                handler.handle(initialContext);
            }

            return initialContext;
        });
    }
}