package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "directory", nullable = false)
    private String directory;

    @Column(name = "album", nullable = false)
    private String album;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "encrypted_key", nullable = false, unique = true)
    private String encryptedKey;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "info")
    private String info;

    @Column(name = "tags")
    private String tags;

    @Column(name = "visibility", nullable = false)
    private Integer visibility;

    @Column(name = "category")
    private String category;

    @Column(name = "access_password")
    private String accessPassword;
}
