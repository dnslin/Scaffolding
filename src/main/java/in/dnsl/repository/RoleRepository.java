package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.Role;

public interface RoleRepository extends EntityGraphJpaRepository<Role, Integer> {
}
