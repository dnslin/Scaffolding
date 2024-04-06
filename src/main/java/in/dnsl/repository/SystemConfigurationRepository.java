package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.SystemConfiguration;

public interface SystemConfigurationRepository extends EntityGraphJpaRepository<SystemConfiguration, Long> {
}
