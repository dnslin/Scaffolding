package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends EntityGraphJpaRepository<Token, Long> {

    // 查找token 去除n+1
    Optional<List<Token>> findByUserId(Long userId);

    Integer deleteByUserId(Long userId);

    Optional<Token> findByToken(String token);

    // 根据用户id和tokenName查找token是否存在
    Optional<Token> findByUserIdAndName(Long userId, String tokenName);

    Optional<Token> findByUserIdAndToken(Long userId, String token);

}
