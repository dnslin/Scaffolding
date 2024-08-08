package in.dnsl.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo implements Serializable {

    private Integer id;

    private String name;

    private String description;

}