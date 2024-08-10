package in.dnsl.model.context;

import in.dnsl.model.entity.UploadConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record UploadContext(
        String ipAddress,
        List<MultipartFile> files,
        UploadConfiguration config,
        Map<String, Object> attributes
) {
    public UploadContext {
        attributes = new ConcurrentHashMap<>();
    }

    public <T> void setAttribute(String key, T value) {
        attributes.put(key, value);
    }

    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }
}