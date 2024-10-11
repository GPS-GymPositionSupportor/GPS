package gps.base.controller;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.exception.GymNotFoundException;
import gps.base.exception.UnauthorizedException;
import gps.base.model.Comment;
import gps.base.model.Review;
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

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);


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
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
    }

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO reviewDTO, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 세션의 userId와 요청의 userId가 일치하는지 확인
        if(sessionUserId == null || !sessionUserId.equals(reviewDTO.getUserId())) {
            throw new UnauthorizedException("로그인이 필요하거나 유효하지 않은 사용자입니다.");
        }

        reviewDTO.setUserId(sessionUserId);

        try {
            ReviewDTO createdReview = reviewService.createReview(reviewDTO);
            return ResponseEntity.ok(createdReview);
        } catch (GymNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
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
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        try {
            Review review = reviewService.getReviewById(rId, reviewDTO.getGymId(), userId);
            if(!Objects.equals(review.getUserId(), userId)) {
                throw new UnauthorizedException("리뷰를 수정할 권한이 없습니다.");
            }
            Review updatedReview = reviewService.updateReview(rId, reviewDTO.getGymId(), userId, reviewDTO);
            return ResponseEntity.ok(updatedReview);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // 리뷰 삭제
    @DeleteMapping("/{rId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long rId, @RequestParam Long gymId , HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        try {
            reviewService.deleteReview(rId, gymId, userId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("리뷰를 삭제할 권한이 없습니다.");
        }
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
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Comment savedComment = reviewService.addComment(rId, userId, commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    // 댓글 수정
    @PutMapping("/{rId}/comments/{cId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long rId, @PathVariable Long cId, @RequestBody CommentDTO commentDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        Comment updatedComment = reviewService.updateComment(rId, cId, userId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{rId}/comments/{cId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long rId, @PathVariable Long cId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        reviewService.deleteComment(rId, cId, userId);
        return ResponseEntity.noContent().build();
    }



}
