package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.SiteConfig;

import java.util.Optional;

public interface SiteConfigRepository extends EntityGraphJpaRepository<SiteConfig, Long> {

    Optional<SiteConfig> findSiteConfigById(Long id);
}
