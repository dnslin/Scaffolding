package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {
    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(nullable = false, length = 255)
    private String avatar;

    @Column(nullable = false)
    private String salt;
    // 默认值为true
    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "last_login_ip", length = 255)
    private String lastLoginIp;
}
