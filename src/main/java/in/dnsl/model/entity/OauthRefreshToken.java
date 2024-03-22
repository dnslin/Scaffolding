package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "oauth_refresh_token")
public class OauthRefreshToken {

    @Id
    @Column(name = "token_id")
    private String tokenId;

    @Lob
    private byte[] token;

    @Lob
    private byte[] authentication;
}
