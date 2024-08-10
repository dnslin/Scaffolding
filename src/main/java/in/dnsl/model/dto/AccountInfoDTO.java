package in.dnsl.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountInfoDTO {

    private Long userId;

    private String username;
}
