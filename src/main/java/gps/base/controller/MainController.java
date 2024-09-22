package gps.base.controller;


import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.*;
import gps.base.service.GymService;
import gps.base.service.MemberService;
import gps.base.service.ReviewService;
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

@Controller
@RequestMapping("/api")
public class MainController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GymService gymService;

    @Autowired
    private ReviewService reviewService;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    /*
    회원관리 
    
    1. 회원가입
    2. 로그인
    3. 프로필 이미지 업데이트

     */

    @GetMapping("/register")
    public String showRegisterForm() {
        return "forward:/registerForm.html";
    }


    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member, Model model) {
        logger.info("회원 등록 요청을 받았습니다. : {}", member.getMId());
        try {
            member.setAuthority(Authority.USER);

            Member savedMember = memberService.saveMember(member);
            logger.info("회원 등록 완료 : {}", savedMember.getMId());
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "forward:/login.html";
        } catch (Exception e) {
            logger.error("회원 등록 실패", e);
            model.addAttribute("error", "회원 등록 실패 : " + e.getMessage());
            return "forward:/register.html";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "forward:/login.html";
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String mId, @RequestParam String mPassword) {
        boolean isAuthenticated = memberService.authenticateMember(mId, mPassword);

        if (isAuthenticated) {
            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }


    @PostMapping("/member/{userId}/profile-image")
    public ResponseEntity<String> updateProfileImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            memberService.updateProfileImage(userId, file);
            return ResponseEntity.ok("프로필 이미지가 업데이트되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        }
    }



    /*
    체육관 관리
     */

    @PostMapping("/gym")
    public ResponseEntity<Gym> registerGym(@RequestBody Gym gym) {
        Gym savedGym = gymService.saveGym(gym);
        return new ResponseEntity<>(savedGym, HttpStatus.CREATED);
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<Gym> getGym(@PathVariable Long gymId) {
        Gym gym = gymService.getGymById(gymId);
        return ResponseEntity.ok(gym);
    }


    /*
    리뷰 관리
     */
    @PostMapping("/review")
    public ResponseEntity<Review> createReview(@RequestBody ReviewDTO reviewDTO) {
        Review savedReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }


    @GetMapping("/gym/{gymId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByGym(@PathVariable Long gymId) {
        List<Review> reviews = reviewService.getReviewsByGym(gymId);
        return ResponseEntity.ok(reviews);
    }


    @PutMapping("/review/{gymId}/{userId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long gymId, @PathVariable Long userId, @RequestBody ReviewDTO reviewDTO) {
        Review updatedReview = reviewService.updateReview(gymId, userId, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }


    @DeleteMapping("/review/{gymId}/{userId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long gymId, @PathVariable Long userId) {
        reviewService.deleteReview(gymId, userId);
        return ResponseEntity.noContent().build();
    }


    /*
    댓글 관리
     */
    @PostMapping("/comment")
    public ResponseEntity<Comment> addComent(@RequestBody CommentDTO commentDTO) {
        Comment savedComment = reviewService.addComment(commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }


    @PutMapping("/comment/{gymId}/{userId}/{cId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long gymId,@PathVariable Long userId, @PathVariable Long cId, @RequestBody CommentDTO commentDTO) {
        Comment updatedComment = reviewService.updateComment(gymId, userId, cId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }


    @DeleteMapping("/comment/{gymId}/{userId}/{cId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long gymId, @PathVariable Long userId, @PathVariable Long cId) {
        reviewService.deleteComment(gymId, userId, cId);
        return ResponseEntity.noContent().build();
    }

}
