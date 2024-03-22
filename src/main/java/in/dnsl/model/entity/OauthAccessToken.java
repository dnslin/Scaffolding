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
@Table(name = "oauth_access_token")
public class OauthAccessToken {
    @Column(name = "token_id")
    private String tokenId;

    @Lob
    private byte[] token;

    @Id
    @Column(name = "authentication_id")
    private String authenticationId;

    @Column
    private String username;

    @Column(name = "client_id")
    private String clientId;

    @Lob
    private byte[] authentication;

    @Column(name = "refresh_token")
    private String refreshToken;
}
