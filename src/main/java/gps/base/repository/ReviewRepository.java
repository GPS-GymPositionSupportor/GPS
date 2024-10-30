package gps.base.repository;


import gps.base.ElasticSearchEntity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 체육관별 리뷰 검색
    List<Review> findByGym_GymId(Long gymId);

    // 사용자별 리뷰 검색
    List<Review> findByReviewId(Long userId);
}