package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Long userId; // 操作用户

    @Column(name = "user_name", nullable = false)
    private String userName; // 操作用户名

    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime; // 操作时间

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType; // 操作类型，如登录、登出、修改信息等

    @Column(name = "operation_ip", nullable = false, length = 30)
    private String operationIp; // 操作IP

    @Column(name = "operation_details", length = 1024)
    private String operationDetails; // 操作详情，可以是详细的操作描述或原因

    @Column(name = "ua", length = 100)
    private String ua; // 创建时间

}
