package gps.base.controller;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.Comment;
import gps.base.model.Gym;
import gps.base.model.Member;
import gps.base.model.Review;
import gps.base.service.GymService;
import gps.base.service.MemberService;
import gps.base.service.ReviewService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GymService gymService;

    @Autowired
    private ReviewService reviewService;




    /*
    회원관리 
    
    1. 회원가입
    2. 로그인
    3. 프로필 이미지 업데이트

     */
    @PostMapping("/register")
    public ResponseEntity<Member> registerMember(@RequestBody Member member) {
        Member savedMember = memberService.saveMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
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
    public ResponseEntity<String> updateProfileImage(@PathVariable Long userId, @RequestParam("file")MultipartFile file) {
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
