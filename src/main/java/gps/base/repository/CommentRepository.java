package gps.base.repository;

import gps.base.model.Comment;
import gps.base.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReview(Review review);


    @Query("SELECT c FROM Comment c JOIN FETCH c.review r ORDER BY c.addedAt DESC")
    Page<Comment> findAllWithUserOrderByAddedAtDesc(Pageable pageable);

}
