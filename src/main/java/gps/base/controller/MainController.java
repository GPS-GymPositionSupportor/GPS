package gps.base.controller;


import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.*;
import gps.base.service.GymService;
import gps.base.service.MemberService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 회원가입 폼
    @GetMapping("/register")
    public String showRegisterForm() {
        return "registerForm";
    }


    // 회원가입 데이터 POST
    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member, Model model) {
        logger.info("회원 등록 요청을 받았습니다. : {}", member.getMId());
        try {
            member.setAuthority(Authority.USER);

            Member savedMember = memberService.saveMember(member);
            logger.info("회원 등록 완료 : {}", savedMember.getMId());
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "/login";
        } catch (Exception e) {
            logger.error("회원 등록 실패", e);
            model.addAttribute("error", "회원 등록 실패 : " + e.getMessage());
            return "/register";
        }
    }


    // 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        return "/login";
    }


    // 로그인 데이터 POST
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String mId, @RequestParam String mPassword, HttpSession session) {
        Member member = memberService.authenticateMember(mId, mPassword);
        Map<String, String> response = new HashMap<>();

        if( member != null ) {
            // 인증 성공 시 회원 정보를 조회하여 세션에 저장
            try {
                session.setAttribute("loggedInUser", member);
                session.setMaxInactiveInterval(1800);   // 세션 30분
                logger.info("User logged in: {}. Session ID : {}", member.getMId(), session.getId());
                response.put("status", "success");
                response.put("message", "로그인 성공");
                response.put("redirect", "/api/main");
                return ResponseEntity.ok(response);
            } catch (EntityNotFoundException e) {
                // 회원 정보 조회 실패 시
                response.put("status", "error");
                response.put("message", "로그인 처리 중 오류 발생");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            logger.warn("Login failed for user : {}", mId);
            response.put("status", "error");
            response.put("message", "로그인 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    // 프로필 Update
    @PostMapping("/member/{userId}/profile-image")
    public ResponseEntity<String> updateProfileImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            memberService.updateProfileImage(userId, file);
            return ResponseEntity.ok("프로필 이미지가 업데이트되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        }
    }


    // 로그 아웃
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        Map<String, String> response = new HashMap<>();

        if(loggedInUser != null) {
            logger.info("User logged out: {}. Session ID : {}", loggedInUser.getMId(), session.getId());
            session.invalidate();
            response.put("status", "success");
            response.put("message", "로그아웃 성공");
            response.put("redirect", "/api/login");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Logout attempted with no user in session. Session ID : {}", session.getId());
            response.put("status", "error");
            response.put("message", "로그아웃 실패: 로그인된 사용자가 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    /*
    메인 페이지

    1. 서버에서 사용자 이름을 Get 하는 Logic
     */

    // 메인 페이지 Form
    @GetMapping("/main")
    public String mainPage(HttpSession session) {
        if(session.getAttribute("loggedInUser") == null) {
            return "redirect:/api/login";
        }
        return "main";
    }

    // 사용자 정보 Get
    @GetMapping("/user-info")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getUserInfo(HttpSession session) {
        logger.debug("getUserInfo called. Session ID : {}", session.getId());

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        if(loggedInUser != null) {
            logger.info("User info requested for: {}", loggedInUser.getMId());
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", loggedInUser.getName());
            return ResponseEntity.ok(userInfo);
        } else {
            logger.warn("User info requested but no user in session, Session ID : {}", session.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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

}
