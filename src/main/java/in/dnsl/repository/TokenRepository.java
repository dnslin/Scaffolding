package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import in.dnsl.model.entity.Token;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends Repository<Token, Long> {

    // 查找token 去除n+1
    Optional<List<Token>> findByUserId(Integer userId, EntityGraph entityGraph);

    Integer deleteByUserId(Integer userId);

}
