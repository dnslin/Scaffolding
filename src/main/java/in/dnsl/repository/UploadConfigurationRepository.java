package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.UploadConfiguration;

import java.util.Optional;

public interface UploadConfigurationRepository extends EntityGraphJpaRepository<UploadConfiguration, Long> {
    Optional<UploadConfiguration> findFirstByOrderByIdAsc();
}
