package gps.base.search.ElasticRepository;


import gps.base.search.ElasticEntity.ElasticReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticReviewRepository extends JpaRepository<ElasticReview, Long> {
    List<ElasticReview> findByGymId(Long gymId);
    List<ElasticReview> findByUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.gymId = :gymId")
    Long countByGymId(@Param("gymId") Long gymId);

    @Query("SELECT AVG(r.rating) FROM ElasticGym r")
    Double getAverageRatingByGymId(@Param("gymId") Long gymId);

    List<ElasticReview> findTop5ByOrderByAddedAtDesc();
}