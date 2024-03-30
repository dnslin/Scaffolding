package in.dnsl.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Setter
@ToString
@Builder
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "wallhaven")
@AllArgsConstructor
public class Wallhaven {

    @Id
    private Integer id;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(nullable = false, length = 255)
    private String page;

    @Column(nullable = false, length = 255)
    private String source;

    @Column(nullable = false, length = 255)
    private Integer status;

    @Column(nullable = false, length = 255)
    private Date date;
}
