package gps.base.controller;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Comment;
import gps.base.model.Image;
import gps.base.model.Review;
import gps.base.repository.CommentRepository;
import gps.base.repository.ImageRepository;
import gps.base.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CommentRepository commentRepository;


    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ImageRepository imageRepository;


    // 리뷰 보드 페이지 반환
    @GetMapping("/board")
    public String getReviewBoard(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        logger.debug("Review Board 접근하는 중....   Session ID: {}, User ID: {}", session.getId(), userId);
        if(userId != null) {
            List<Review> reviews = reviewService.getAllReviews();
            model.addAttribute("reviews", reviews);
            model.addAttribute("userId", userId);
            return "board";
        } else {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam("gymId") Long gymId,
            @RequestParam("userId") Long userId,
            @RequestParam("comment") String comment,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");


        if(sessionUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if(!sessionUserId.equals(userId)) {
            throw new CustomException(ErrorCode.WRITER_DOES_NOT_MATCH);
        }

        // ReviewDTO 생성
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .userId(userId)
                .gymId(gymId)
                .comment(comment)
                .build();

        try {
            ReviewDTO createdReview = reviewService.createReview(reviewDTO, file);
            return ResponseEntity.ok(createdReview);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    // 특정 체육관의 리뷰 가져오기
    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<Review>> getReviewsByGym(@PathVariable long gymId) {
        List<Review> reviews = reviewService.getReviewsByGym(gymId);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 수정
    @PutMapping("/{rId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long rId, @RequestBody ReviewDTO reviewDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReviewAndImage(rId, reviewDTO.getGymId(), userId, null);  // 이미지 없이 검증
        Review updatedReview = reviewService.updateReview(rId, reviewDTO.getGymId(), userId, reviewDTO);
        return ResponseEntity.ok(updatedReview);

    }

    // 리뷰 삭제
    @DeleteMapping("/{rId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long rId, @RequestParam Long gymId , HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReviewAndImage(rId, gymId, userId, null);  // 이미지 없이 검증
        reviewService.deleteReview(rId, gymId, userId);
        return ResponseEntity.ok().build();
    }


    // 모든 리뷰 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviewsWithUserName();
        return ResponseEntity.ok(reviews);
    }






    /*
    댓글 관리
     */
    
    
    // 댓글 추가
    @PostMapping("/{rId}/comments")
    public ResponseEntity<Comment> addComent(@PathVariable Long rId, @RequestBody CommentDTO commentDTO, HttpSession session) {
        Long userId =  (Long) session.getAttribute("userId");
        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReview(rId, userId);

        Comment savedComment = reviewService.addComment(rId, userId, commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    // 댓글 수정
    @PutMapping("/{rId}/comments/{cId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long rId, @PathVariable Long cId, @RequestBody CommentDTO commentDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        // 로그인 여부 확인
        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReviewAndComment(rId, cId, userId);
        Comment updatedComment = reviewService.updateComment(rId, cId, userId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{rId}/comments/{cId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long rId, @PathVariable Long cId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        // 로그인 여부 확인
        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReviewAndComment(rId, cId, userId);

        reviewService.deleteComment(rId, cId, userId);
        return ResponseEntity.noContent().build();
    }



}
