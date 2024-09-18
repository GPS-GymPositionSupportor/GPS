package gps.base.service;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.Comment;
import gps.base.model.Review;
import gps.base.repository.CommentRepository;
import gps.base.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Review createReview(ReviewDTO reviewDTO) {
        Review review = new Review();

        // DTO 에서 엔티티로 데이터 복사
        review.setUserId(reviewDTO.getUserId());
        review.setGymId(reviewDTO.getGymId());
        review.setComment(reviewDTO.getComment());

        Review savedReview = reviewRepository.save(review);

        // websocket을 통해 실시간 알림 전송
        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "새로운 리뷰가 작성되었습니다.");

        return savedReview;
    }

    public List<Review> getReviewsByGym(Long gymId) {
        return reviewRepository.findByGymId(gymId);
    }

    public Comment addComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUserId(commentDTO.getUserId());
        comment.setGymId(commentDTO.getGymId());
        comment.setComment(commentDTO.getComment());

        Comment savedComment = commentRepository.save(comment);

        // WebSocket을 통해 실시간 알림 전송
        messagingTemplate.convertAndSend("/topic/gym/" + comment.getGymId(), "새로운 댓글이 작성되었습니다.");

        return savedComment;
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(reviewId + " 에 해당하는 리뷰는 존재하지 않습니다."));
    }

    public List<Comment> getCommentsByGym(Long gymId) {
        return commentRepository.findByGymId(gymId);
    }
    
    
    // 리뷰 수정 , 삭제 메소드 구현

}
