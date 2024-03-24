package in.dnsl.model.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {

    private Long id;

    private String username;


    private String email;

    private String nickname;

    private String avatar;

    private SaTokenInfo TokenInfo;
}
