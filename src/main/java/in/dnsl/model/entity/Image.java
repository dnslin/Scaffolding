package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "userId", nullable = false)
    private Integer userId;

    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "directory", nullable = false)
    private String directory;

    @Column(name = "album", nullable = false)
    private String album;

    @Temporal(TemporalType.DATE)
    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "modifyDate", nullable = false)
    private Date modifyDate;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Column(name = "encryptedKey", nullable = false, unique = true)
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

    @Column(name = "accessPassword")
    private String accessPassword;
}
