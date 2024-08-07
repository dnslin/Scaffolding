package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "site_config")
@NoArgsConstructor
@AllArgsConstructor
public class SiteConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "site_name")
    private String siteName;

    @Column(nullable = false,name = "site_url")
    private String logoUrl;

    @Column(nullable = false,name = "favicon_url")
    private String faviconUrl;

    @Column(nullable = false,name = "site_description")
    private String siteDescription;

    @Column(nullable = false,name = "site_keywords")
    private String copyright;

    @Column(nullable = false,name = "contact_email")
    private String contactEmail;

    @Column(nullable = false,name = "contact_qq")
    private String announcement;
}
