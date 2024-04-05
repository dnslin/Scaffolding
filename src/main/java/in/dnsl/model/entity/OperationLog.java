package in.dnsl.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @ManyToOne
    // @JoinColumn(name = "user_id", referencedColumnName = "id")
    // private User user;
    @Column(name = "user_id")
    private Long userId; // 操作用户

    @Column(name = "user_name", nullable = false)
    private String userName; // 操作用户名

    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime; // 操作时间

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType; // 操作类型，如登录、登出、修改信息等

    @Column(name = "operation_ip", nullable = false, length = 255)
    private String operationIp; // 操作IP

    @Column(name = "operation_details", length = 1024)
    private String operationDetails; // 操作详情，可以是详细的操作描述或原因

    @Column(name = "ua", length = 255)
    private String ua; // 创建时间


    public OperationLog(Integer id, Long userId, String userName, LocalDateTime operationTime, String operationType, String operationIp, String operationDetails, String ua) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.operationTime = operationTime;
        this.operationType = operationType;
        this.operationIp = operationIp;
        this.operationDetails = operationDetails;
        this.ua = ua;
    }
}
