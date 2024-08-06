package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.Permission;


public interface PermissionRepository extends EntityGraphJpaRepository<Permission, Integer> {
}
