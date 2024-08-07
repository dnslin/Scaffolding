package in.dnsl.model.entity;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "system_configurations")
public class SystemConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "thumbnail_enabled", nullable = false)
    private Boolean thumbnailEnabled;

    @Column(name = "blacklist_enabled")
    private String blacklistEnabled;

    @Column(name = "login_upload_enabled", nullable = false)
    private Boolean loginUploadEnabled;

    @Column(name = "api_upload_enabled", nullable = false)
    private Boolean apiUploadEnabled;

    @Column(name = "encryption_enabled", nullable = false)
    private Boolean encryptionEnabled;

    @Column(name = "trash_bin_enabled", nullable = false)
    private Boolean trashBinEnabled;

    @Column(name = "delete_link_enabled", nullable = false)
    private Boolean deleteLinkEnabled;

    @Column(name = "upload_log_enabled", nullable = false)
    private Boolean uploadLogEnabled;

    @Column(name = "login_log_enabled", nullable = false)
    private Boolean loginLogEnabled;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "guest_upload_limit", nullable = false)
    private String guestUploadLimit;

    @Column(name = "image_compression_enabled", nullable = false)
    private Boolean imageCompressionEnabled;

    @Column(name = "image_conversion_enabled", nullable = false)
    private Boolean imageConversionEnabled;

    @Column(name = "trash_bin_days", nullable = false)
    private Integer trashBinDays;

    @Column(name = "enable_compression", nullable = false)
    private Boolean enableCompression;

    @Column(name = "allow_format_conversion", nullable = false)
    private Boolean allowFormatConversion;

}
