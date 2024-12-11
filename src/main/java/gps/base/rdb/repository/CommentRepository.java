package gps.base.rdb.repository;

import gps.base.rdb.model.Comment;
import gps.base.rdb.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReview(Review review);


    @Query("SELECT c FROM Comment c JOIN FETCH c.review r ORDER BY c.addedAt DESC")
    Page<Comment> findAllWithUserOrderByAddedAtDesc(Pageable pageable);

}
