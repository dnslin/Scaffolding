package in.dnsl.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenProVo {

    private String name;

    private String token;

    private LocalDateTime creationTime;

    private LocalDateTime expirationTime;
}
