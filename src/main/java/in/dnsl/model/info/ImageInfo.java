package in.dnsl.model.info;


import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class ImageInfo {
    private final Map<String, List<TagInfo>> directoryInfo = new HashMap<>();
    private final List<String> errors = new ArrayList<>();

    public void addTag(String directory, String tagName, String description) {
        directoryInfo.computeIfAbsent(directory, k -> new ArrayList<>())
                .add(new TagInfo(tagName, description));
    }

    public void addError(String error) {
        errors.add(error);
    }

    public record TagInfo(String name, String description) {
    }
}