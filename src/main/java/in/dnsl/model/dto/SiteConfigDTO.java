package in.dnsl.model.dto;

import lombok.Data;

@Data
public class SiteConfigDTO {

    /**
     * 网站的名称
     */
    private String siteName;

    /**
     * 网站LOGO的URL
     */
    private String logoUrl;

    /**
     * 网站的Favicon的URL
     */
    private String faviconUrl;

    /**
     * 网站的描述
     */
    private String siteDescription;

    /**
     * 网站的归属关键字
     */
    private String copyright;

    /**
     * 网站的联系邮箱
     */
    private String contactEmail;

    /**
     * 网站的联系公告
     */
    private String announcement;
}
