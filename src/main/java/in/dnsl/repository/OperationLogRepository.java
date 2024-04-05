package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.OperationLog;

public interface OperationLogRepository extends EntityGraphJpaRepository<OperationLog, Long> {

}