package gps.base.repository;

import gps.base.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByReviewId(Long reviewId);
    List<Image> findByGymId(Long gymId);
    List<Image> findByUserId(Long userId);
    void deleteByReviewId(Long reviewId);
}
