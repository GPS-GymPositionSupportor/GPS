package gps.base.controller;


import gps.base.DTO.MemberDTO;
import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
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
        return "index";
    }


    // 로그인 데이터 POST
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Member member = memberService.authenticateMember(username, password);

            // 로그인 실패
            if(member == null) {
                session.setAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
                return "redirect:/";
            }

            // 로그인 성공: 세션 설정
            session.setAttribute("loggedInUser", member);
            session.setAttribute("userId", member.getUserId());
            session.setAttribute("nickname", member.getNickname());
            session.setAttribute("name", member.getName());
            session.setAttribute("authority", member.getAuthority());
            session.setMaxInactiveInterval(1800);

            // 권한 체크 및 리다이렉션
            String authority = String.valueOf(member.getAuthority());
            if ("ADMIN".equals(authority)) {
                return "redirect:/api/admin";
            }
            return "redirect:/";

        } catch (Exception e) {
            session.setAttribute("loginError", "로그인 처리 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }


    // 프로필 정보 업데이트 (이미지 포함)
    @PostMapping("/member/{userId}/myProfile")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "profileData") MemberDTO profileData) {
        try {
            // 이미지가 있다면 업데이트
            if (file != null && !file.isEmpty()) {
                memberService.updateProfileImage(userId, file);
            }

            // 프로필 정보 업데이트
            memberService.updateProfileInfo(userId, profileData);

            return ResponseEntity.ok("프로필이 업데이트되었습니다.");
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
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
        return "index";
    }


    // 사용자 정보 Get
    @GetMapping("/user-info")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getUserInfo(HttpSession session) {
        logger.debug("getUserInfo called. Session ID : {}", session.getId());

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        if(loggedInUser != null) {
            logger.info("User info requested for: {}", loggedInUser.getUserId());
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", loggedInUser.getName());
            userInfo.put("userId", String.valueOf(loggedInUser.getUserId()));
            return ResponseEntity.ok(userInfo);
        } else {
            logger.warn("User info requested but no user in session, Session ID : {}", session.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /*
    어드민 관련
     */

    @GetMapping("/admin")
    public String getMainPage(HttpSession session) {
        return "admin";
    }




    /*
    체육관 관리
     */

    // HTMl 호출
    @GetMapping("/showgyms")
    public String showGymList(Model model, HttpSession session) {

        if(session.getAttribute("loggedInUser") == null) {
            return "redirect:/api/login";
        }

        List<Gym> gyms = gymService.getAllGyms();
        model.addAttribute("gyms",  gyms);
        return "gym";
    }

    // API 엔드포인트 추가
    @GetMapping("/gyms")
    @ResponseBody  // JSON 응답을 위해 필요
    public List<Gym> getGyms(HttpSession session) {
        if(session.getAttribute("loggedInUser") == null) {
            // 로그인 안 된 경우 빈 리스트 반환하거나 예외 처리
            return new ArrayList<>();
        }
        return gymService.getAllGyms();
    }



    // 특정 체육관 ID 조회
    @GetMapping("/gym/{gymId}")
    @ResponseBody
    public ResponseEntity<?> getGym(@PathVariable Long gymId) {
        try {
            Map<String, Object> gymDetail = gymService.getGymDetailById(gymId);
            return ResponseEntity.ok(gymDetail);
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.GYM_NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


}
