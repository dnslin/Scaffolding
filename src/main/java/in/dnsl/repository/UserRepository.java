package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public interface UserRepository extends EntityGraphJpaRepository<User, Long>{

    // 根据用户名查询用户
    Optional<User> findByUsername(String username);
}