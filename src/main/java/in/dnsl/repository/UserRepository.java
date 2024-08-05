package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public interface UserRepository extends EntityGraphJpaRepository<User, Long>{

    // 根据用户名以及状态查询用户
    Optional<User> findByUsernameAndEnabled(String username, Boolean disable);

    // 根据ID以及状态查询用户
    Optional<User> findByIdAndEnabled(Long id, Boolean disable);

    // 判断用户是否存在
    boolean existsByUsername(String username);

    // 根据ID进行判断用户是否存在
    boolean existsById(@NotNull Long id);

    // 查询所有的用户和ID
    List<User> findAllByEnabled(Boolean enabled);
}