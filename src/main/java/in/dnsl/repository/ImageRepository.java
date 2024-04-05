package in.dnsl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import in.dnsl.model.entity.Image;

public interface ImageRepository extends EntityGraphJpaRepository<Image, Long> {

}
