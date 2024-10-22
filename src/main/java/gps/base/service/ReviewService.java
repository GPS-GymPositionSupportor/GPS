package gps.base.service;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.exception.GymNotFoundException;
import gps.base.model.Comment;
import gps.base.model.Gym;
import gps.base.model.Member;
import gps.base.model.Review;
import gps.base.repository.CommentRepository;
import gps.base.repository.GymRepository;
import gps.base.repository.MemberRepository;
import gps.base.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GymRepository gymRepository;

    @Value("${upload.path}")
    private String uploadPath;
    
    
    /*
    리뷰 관련
     */

    // 리뷰 작성
    public ReviewDTO createReview(ReviewDTO reviewDTO, MultipartFile file) throws IOException, GymNotFoundException {
        Review review = new Review();

        if(!gymRepository.existsById(reviewDTO.getGymId())) {
            throw new GymNotFoundException(reviewDTO.getGymId());
        }

        // DTO 에서 엔티티로 데이터 복사
        review.setUserId(reviewDTO.getUserId());
        review.setGymId(reviewDTO.getGymId());
        review.setComment(reviewDTO.getComment());

        if(reviewDTO.getFile() != null && !reviewDTO.getFile().isEmpty()) {
            String imagePath = saveImage(reviewDTO.getFile());
        }

        Review savedReview = reviewRepository.save(review);

        // websocket을 통해 실시간 알림 전송
        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "새로운 리뷰가 작성되었습니다.");

        return convertToDTO(savedReview);
    }

    private String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath + File.separator + fileName);
        Files.copy(file.getInputStream(), path);
        return fileName;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setRId(review.getRId());
        dto.setUserId(review.getUserId());
        dto.setGymId(review.getGymId());
        dto.setComment(review.getComment());
        dto.setAddedAt(review.getAddedAt());

        // userId를 이용해서 Member 정보를 조회하고 이름을 설정
        memberRepository.findById(review.getUserId())
                .ifPresent(member -> dto.setUserName(member.getName()));

        return dto;
    }

    // Gym_id 값으로 리뷰 가져오기
    public List<Review> getReviewsByGym(Long gymId) {
        return reviewRepository.findByGymId(gymId);
    }

    // 리뷰 ID로 단일 리뷰 가져오기
    public Review getReviewById(Long rId, Long gymId, Long userId) {
        return reviewRepository.findByrIdAndGymIdAndUserId(rId, gymId, userId)
                .orElseThrow(() -> new EntityNotFoundException(rId + " 에 해당하는 리뷰는 존재하지 않습니다."));
    }

    // 모든 리뷰 가져오기
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }


    // Review와 Member 정보 함께 가져오기
    public List<ReviewDTO> getAllReviewsWithUserName() {
        List<ReviewDTO> reviews = reviewRepository.findAllReviewsWithUserName();
        return reviews.stream().map(review -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setRId(review.getRId());
            dto.setAddedAt(review.getAddedAt());
            dto.setUserId(review.getUserId());
            dto.setGymId(review.getGymId());
            dto.setComment(review.getComment());

            // 사용자 이름 가져오기
            Member member = memberRepository.findById(review.getUserId()).orElse(null);
            dto.setUserName(member != null ? member.getName() : "Unknown");
            return dto;
        }).collect(Collectors.toList());
    }
    
    
    // 리뷰 수정
    @Transactional
    public Review updateReview(Long rId , Long gymId, Long userId, ReviewDTO reviewDTO) {
        Review review = getReviewById(rId, gymId, userId);
        review.setComment(reviewDTO.getComment());
        // 추후 다른 필드들도 업데이트가 필요하면 코드추가

        Review updatedReview = reviewRepository.save(review);
        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 수정되었습니다.");
        return updatedReview;
    }


    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long rId, Long gymId, Long userId) {
        Review review = getReviewById(rId, gymId, userId);
        reviewRepository.delete(review);
        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 삭제되었습니다.");
    }

    
    /*
    댓글 관련
     */


    // 댓글 작성
    public Comment addComment(Long rId, Long userId,  CommentDTO commentDTO) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setReview(review);
        comment.setComment(commentDTO.getComment());
        Comment savedComment = commentRepository.save(comment);

        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "새로운 댓글이 작성되었습니다.");
        return savedComment;

    }

    // 리뷰에 대한 댓글 가져오기
    public List<Comment> getCommentsByReview(Long rId) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));
        return commentRepository.findByReview(review);
    }
    
    
    
    // 댓글 수정
    @Transactional
    public Comment updateComment(Long rId, Long commentId, Long userId, CommentDTO commentDTO) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        if(!comment.getUserId().equals(userId) || !comment.getReview().equals(rId)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setComment(commentDTO.getComment());
        Comment updatedComment = commentRepository.save(comment);
        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "댓글이 수정되었습니다.");
        return updatedComment;
    }



    // 댓글 삭제
    @Transactional
    public void deleteComment(Long rId, Long commentId, Long userId) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        if(!comment.getUserId().equals(userId) || !comment.getReview().equals(rId)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "댓글이 삭제되었습니다.");
    }

    // gym_id 값에 의해서 gym 데이터 요청
    public List<Comment> getCommentsByGym(Long gymId) {
        List<Review> reviews = getReviewsByGym(gymId);
        List<Comment> comments = new ArrayList<>();
        for (Review review : reviews) {
            comments.addAll(commentRepository.findByReview(review));
        }

        return comments;
    }

}
