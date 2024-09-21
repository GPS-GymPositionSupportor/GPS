package gps.base.repository;

import gps.base.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByGymIdAndUserIdAndId(Long gymId, Long userId, Long id);
    List<Comment> findByGymId(Long reviewId);
}
