package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.Wallhaven;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WallhavenRepository extends EntityGraphJpaRepository<Wallhaven, Integer> {

    List<Wallhaven> findWallhavenByStatus(Integer status);

}
