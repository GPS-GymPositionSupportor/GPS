package gps.base.search.ElasticRepository;


import gps.base.search.ElasticEntity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGymId(Long gymId);
    List<Review> findByUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.gymId = :gymId")
    Long countByGymId(@Param("gymId") Long gymId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.gymId = :gymId")
    Double getAverageRatingByGymId(@Param("gymId") Long gymId);

    List<Review> findTop5ByOrderByAddedAtDesc();
}