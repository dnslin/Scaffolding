package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.Role;

import java.util.Optional;

public interface RoleRepository extends EntityGraphJpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
