package gps.base.service;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.Comment;
import gps.base.model.Review;
import gps.base.repository.CommentRepository;
import gps.base.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    
    // 리뷰 작성
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

    // Gym_id 값으로 리뷰 가져오기
    public List<Review> getReviewsByGym(Long gymId) {
        return reviewRepository.findByGymId(gymId);
    }

    
    // 댓글 작성
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
    
    
    // 리뷰 수정
    @Transactional
    public Review updateReview(Long gymId, Long userId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findByGymIdAndUserId(gymId, userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));

        review.setComment(reviewDTO.getComment());
        // 추후 다른 필드들도 업데이트가 필요하면 코드추가

        Review updatedReview = reviewRepository.save(review);

        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 수정되었습니다.");

        return updatedReview;
    }


    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long gymId, Long userId) {
        Review review = reviewRepository.findByGymIdAndUserId(gymId, userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));

        reviewRepository.delete(review);

        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 삭제되었습니다.");
    }
    
    
    
    // 댓글 수정
    @Transactional
    public Comment updateComment(Long gymId, Long userId, Long cId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findByGymIdAndUserId(gymId, userId, cId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        comment.setComment(commentDTO.getComment());
        // 필요한 경우에 다른 필드들도 추가할 수 있는 코드 작성

        Comment updatedComment = commentRepository.save(comment);

        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "댓글이 수정되었습니다.");

        return updatedComment;
    }



    // 댓글 삭제
    @Transactional
    public void deleteComment(Long gymId, Long userId, Long cId) {
        Comment comment = commentRepository.findByGymIdAndUserId(gymId, userId, cId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);

        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "댓글이 삭제되었습니다.");
    }
    
    
    

}
