package gps.base.rdb.controller;

import gps.base.rdb.DTO.CommentDTO;
import gps.base.rdb.DTO.ReviewDTO;
import gps.base.rdb.error.ErrorCode;
import gps.base.rdb.error.exception.CustomException;
import gps.base.rdb.model.Authority;
import gps.base.rdb.model.Comment;
import gps.base.rdb.model.Member;
import gps.base.rdb.model.Review;
import gps.base.rdb.service.CommentService;
import gps.base.rdb.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CommentService commentService;


    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);


    // 일반 리뷰 페이지 반환
    @GetMapping("myReview")
    public String myReview(HttpSession session) {
        if(session.getAttribute("loggedInUser") == null) {
            return "redirect:/api/login";
        }

        return "myReview";
    }


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

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam("files") List<MultipartFile> files,  // files라는 이름으로 받아야 함
            ReviewDTO reviewDTO,
            HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");

        if(sessionUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if(!sessionUserId.equals(reviewDTO.getUserId())) {
            throw new CustomException(ErrorCode.WRITER_DOES_NOT_MATCH);
        }

        log.info("Received files size: {}", files != null ? files.size() : 0);
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> log.info("File name: {}", file.getOriginalFilename()));
        }

        try {
            ReviewDTO createdReview = reviewService.createReview(reviewDTO, files);
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
        Authority authority = (Authority) session.getAttribute("authority");

        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReviewAndImage(rId, reviewDTO.getGymId(), userId, null, authority);  // 이미지 없이 검증
        Review updatedReview = reviewService.updateReview(rId, reviewDTO.getGymId(), userId, reviewDTO, authority);
        return ResponseEntity.ok(updatedReview);

    }

    // 리뷰 삭제
    @DeleteMapping("/{rId}/{gymId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long rId, @PathVariable Long gymId , HttpSession session) {
        Long userId = (Long) session.getAttribute("userID");
        Authority authority = (Authority) session.getAttribute("authority");
        if(userId == null) {
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
        }

        reviewService.validateReviewAndImage(rId, gymId, userId, null, authority);  // 이미지 없이 검증
        reviewService.deleteReview(rId, gymId, userId, authority);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/all")
    public ResponseEntity<Page<ReviewDTO>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewDTO> reviews = reviewService.getAllReviewsWithPaging(pageRequest);

        // 날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        reviews.getContent().forEach(review ->
                review.setFormattedDate(
                        review.getAddedAt() != null ? review.getAddedAt().format(formatter) : ""
                )
        );

        return ResponseEntity.ok(reviews);
    }
    
    
    // 로그인된 사용자의 모든 리뷰 가져오기
    @GetMapping("/myReviews")
    public ResponseEntity<Page<ReviewDTO>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            HttpSession session
    ) {
        // 세션에서 로그인한 사용자 정보 가져오기
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 페이지 요청 객체 생성
        PageRequest pageRequest = PageRequest.of(page, size);

        // 로그인한 사용자의 리뷰만 조회
        Page<ReviewDTO> reviews = reviewService.getReviewsByUserId(loggedInUser.getUserId(), pageRequest);

        // 날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        reviews.getContent().forEach(review ->
                review.setFormattedDate(
                        review.getAddedAt() != null ? review.getAddedAt().format(formatter) : ""
                )
        );

        return ResponseEntity.ok(reviews);
    }






    /*
    댓글 관리
     */

    // 댓글 가져오기
    @GetMapping("/comments/all")
    @ResponseBody
    public Page<CommentDTO> getAllComments(@PageableDefault(size = 24) Pageable pageable) {

        log.info("댓글 조회 요청 - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<CommentDTO> comments = commentService.getAllComments(pageable);
        log.info("조회된 댓글 수: {}", comments.getContent().size());
        log.info("전체 페이지: {}", comments.getTotalPages());
        log.info("전체 댓글 수: {}", comments.getTotalElements());

        return comments;  // ResponseEntity 제거
    }
    
    
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
    public ResponseEntity<Comment> updateComment(@PathVariable Long rId, @PathVariable Long cId, @RequestBody CommentDTO commentDTO, Authority authority, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        // 로그인 여부 확인
        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReviewAndComment(rId, cId, userId, authority);
        Comment updatedComment = reviewService.updateComment(rId, cId, userId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{rId}/comments/{cId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long rId, @PathVariable Long cId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userID");
        Authority authority = (Authority) session.getAttribute("authority");
        
        // 로그인 여부 확인
        if(userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewService.validateReviewAndComment(rId, cId, userId,authority);

        reviewService.deleteComment(rId, cId, userId, authority);
        return ResponseEntity.noContent().build();
    }



}
