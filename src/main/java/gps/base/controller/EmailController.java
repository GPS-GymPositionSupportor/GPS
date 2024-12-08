package gps.base.controller;

import gps.base.DTO.FavoriteDTO;
import gps.base.service.EmailService;
import gps.base.service.FavoriteService;
import gps.base.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FavoriteService favoriteService;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    
    /*
    회원가입 관련
     */

    // 회원가입을 위한 이메일 인증 코드 전송
    @PostMapping("/send-verification-code-registration")
    public String sendVerificationCodeRegistration(@RequestParam String email, HttpServletRequest request) {
        try {
            emailService.sendVerificationCodeRegistration(email).get(); // 비동기 작업 완료 대기
            request.setAttribute("email", email); // 이메일을 전달
            return "forward:/verifyEmail.jsp"; // 인증 코드 입력 페이지로 포워드
        } catch (Exception e) {
            request.setAttribute("emailError", "메일 전송 실패: " + e.getMessage());
            return "forward:/register.jsp";
        }
    }
    
    /*
    아이디 & 비밀번호 관련
     */
    
    
    // 아이디 & 비밀번호 찾기 폼
    @GetMapping("/findIdPassword")
    public String findIdPassword(Model model) {
        return "findIdPw";
    }

    // 아이디 찾기
    @PostMapping("/send-verification-code-id")
    public CompletableFuture<String> sendVerificationCode(@RequestParam String name, @RequestParam String email) {
        try {
            return emailService.sendVerificationCodeId(name, email)
                    .thenApply(aVoid -> "메일 전송 완료");
        } catch (IllegalArgumentException e) {
            return CompletableFuture.completedFuture("에러: " + e.getMessage());
        } catch (MailException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("메일 전송 실패");
        }
    }
    // 인증코드 비교 및 아이디 전송
    @PostMapping("/verify-code-id")
    public @ResponseBody ResponseEntity<String> verifyCode(@RequestParam String code, @RequestParam String email) {
        String redisCode =  redisTemplate.opsForValue().get(email + ":verificationCode"); // Redis에서 인증 코드 가져오기
        if (redisCode != null && redisCode.equals(code)) { // 인증 코드 비교
            emailService.sendUserId(email); // 인증이 성공하면 유저 아이디 전송
            return ResponseEntity.ok("인증 성공 및 아이디가 이메일로 전송되었습니다.");
        } else {
            return ResponseEntity.status(400).body("인증 실패");
        }
    }

    // 비밀번호 찾기
    @PostMapping("/send-verification-code-pw")
    public @ResponseBody CompletableFuture<ResponseEntity<String>> sendVerificationCodeForPassword(@RequestParam String mId, @RequestParam String email) {
        try {
            return emailService.sendVerificationCodePassword(mId, email)
                    .thenApply(aVoid -> ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다."));
        } catch (IllegalArgumentException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(400).body(e.getMessage()));
        } catch (MailException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body("메일 전송 실패"));
        }
    }
    // 인증코드 비교 및 임시 비밀번호 전송
    @PostMapping("/verify-code-pw")
    public @ResponseBody ResponseEntity<String> verifyCodeForPassword(@RequestParam String code, @RequestParam String email) {
        String redisCode = redisTemplate.opsForValue().get(email + ":verificationCode"); // Redis에서 인증 코드 가져오기
        if (redisCode != null && redisCode.equals(code)) { // 인증 코드 비교
            emailService.sendTemporaryPassword(email); // 인증 성공 시 임시 비밀번호 전송
            return ResponseEntity.ok("인증 성공! 임시 비밀번호가 이메일로 전송되었습니다.");
        } else {
            return ResponseEntity.status(400).body("인증 실패");
        }
    }
    
    
    /*
    즐겨 찾기 관련
     */

    // 즐겨찾기 추가
    @PostMapping("/favorite/add")
    public ResponseEntity<FavoriteDTO> addFavorite(@RequestParam Long userId, @RequestParam Long gymId) {
        FavoriteDTO favoriteDto = favoriteService.addFavorite(userId, gymId);
        return ResponseEntity.ok(favoriteDto);
    }

    // 즐겨찾기 삭제
    @PostMapping("/favorite/remove")
    public ResponseEntity<Void> removeFavoriteUsingPost(@RequestParam Long userId, @RequestParam Long gymId) {
        favoriteService.removeFavorite(userId, gymId);
        return ResponseEntity.noContent().build();
    }

    // 특정 유저의 즐겨찾기 조회
    @GetMapping("/favorite/user/{userId}")
    public String getUserFavorites(@PathVariable Long userId, Model model) {
        List<FavoriteDTO> favorites = favoriteService.getUserFavorites(userId);
        model.addAttribute("favoriteList", favorites);
        return "favoriteTest";
    }
}
