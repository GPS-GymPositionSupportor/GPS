package gps.base.repository;

import gps.base.ElasticSearchEntity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGymId(Long gymId);
    List<Review> findByUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.gymId = :gymId")
    Long countByGymId(Long gymId);

    List<Review> findTop5ByOrderByAddedAtDesc();
}