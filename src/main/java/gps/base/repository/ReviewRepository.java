package gps.base.repository;

import gps.base.DTO.ReviewWithUserNameDTO;
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

    @Query("SELECT new gps.base.DTO.ReviewWithUserNameDTO(r.rId as rId, r.addedAt, r.userId, m.name, r.gymId, r.comment) " +
            "FROM Review r, Member m WHERE r.userId = m.userId")
    List<ReviewWithUserNameDTO> findAllReviewsWithUserName();
}
