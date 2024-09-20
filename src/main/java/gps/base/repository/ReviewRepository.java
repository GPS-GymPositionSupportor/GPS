package gps.base.repository;

import gps.base.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByGymIdAndUserId(Long gymId, Long userId);
    List<Review> findByGymId(Long gymId);
}
