package gps.base.repository;

import gps.base.DTO.ReviewDTO;
import gps.base.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGymId(Long gymId);

    Optional<Review> findByrIdAndGymIdAndUserId(Long rId, Long gymId, Long userId);



    // Review와 Member 정보를 동시에 가져오기 (외래키)
    @Query("SELECT new gps.base.DTO.ReviewDTO(r.rId as rId, r.addedAt, r.userId, r.gymId, m.name, r.comment) " +
            "FROM Review r JOIN Member m ON r.userId = m.userId")
    List<ReviewDTO> findAllReviewsWithUserName();


}
