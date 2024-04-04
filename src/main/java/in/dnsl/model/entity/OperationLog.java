package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // 操作用户

    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime; // 操作时间

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType; // 操作类型，如登录、登出、修改信息等

    @Column(name = "operation_ip", nullable = false, length = 255)
    private String operationIp; // 操作IP

    @Column(nullable = false, length = 50)
    private String status; // 操作结果状态，如成功、失败

    @Column(name = "operation_details", length = 1024)
    private String operationDetails; // 操作详情，可以是详细的操作描述或原因

    @Column(length = 255)
    private String reason; // 操作失败原因（如果有）
}
