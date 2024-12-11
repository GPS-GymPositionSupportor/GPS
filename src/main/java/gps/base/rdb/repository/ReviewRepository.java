package gps.base.rdb.repository;

import gps.base.rdb.DTO.ReviewDTO;
import gps.base.rdb.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Query("SELECT new gps.base.rdb.DTO.ReviewDTO(r.rId, r.addedAt, r.userId, r.gymId, m.name, r.comment) " +
            "FROM Review r JOIN Member m ON r.userId = m.userId " +
            "ORDER BY r.addedAt DESC")
    List<ReviewDTO> findAllReviewsWithUserName();


    Page<Review> findByUserIdOrderByAddedAtDesc(Long userId, PageRequest pageRequest);
}
