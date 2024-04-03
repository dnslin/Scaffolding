package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_config")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "allowed_extensions", nullable = false)
    private String allowedExtensions;

    @Column(name = "naming_strategy", nullable = false)
    private String namingStrategy;

    @Column(name = "allow_format_conversion", nullable = false)
    private Boolean allowFormatConversion;

    @Column(name = "max_file_size", nullable = false)
    private Integer maxFileSize;

    @Column(name = "min_file_size", nullable = false)
    private Integer minFileSize;

    @Column(name = "max_upload_count", nullable = false)
    private Integer maxUploadCount;

    @Column(name = "max_upload_width", nullable = false)
    private Integer maxUploadWidth;

    @Column(name = "min_upload_width", nullable = false)
    private Integer minUploadWidth;

    @Column(name = "max_upload_height", nullable = false)
    private Integer maxUploadHeight;

    @Column(name = "min_upload_height", nullable = false)
    private Integer minUploadHeight;

    @Column(name = "enable_compression", nullable = false)
    private Boolean enableCompression;
}
