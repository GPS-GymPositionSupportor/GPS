package gps.base.controller;

import gps.base.DTO.EmailRequest;
import gps.base.model.Authority;
import gps.base.repository.MemberRepository;
import gps.base.service.EmailService;
import gps.base.service.MemberService;
import gps.base.model.Member;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class MainController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/find-userId")
    public String showFindUserIdForm() {
        return "find-userId";
    }

    @GetMapping("/send-email-form")
    public String showSendEmailForm(Model model) {
        model.addAttribute("emailRequest", new EmailRequest());
        return "send-email";
    }

    @PostMapping("/send-verification-code")
    public @ResponseBody ResponseEntity<?> sendVerificationCode(@RequestParam String email) {
        try {
            int number = emailService.sendMail(email);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "메일 전송 완료, 인증번호: " + number));
        } catch (MailException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "메일 전송 실패"));
        }
    }

    @PostMapping("/verify-code")
    public @ResponseBody ResponseEntity<?> verifyCode(@RequestParam int code) {
        if (code == EmailService.number) { // 인증 번호 비교
            return ResponseEntity.ok().body(Collections.singletonMap("message", "인증 성공"));
        } else {
            return ResponseEntity.status(400).body(Collections.singletonMap("message", "인증 실패"));
        }
    }

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
        Map<String, String> response = new HashMap<>();

        try {
            Member member = memberService.authenticateMember(mId, mPassword);

            if(member != null) {
                // 인증 성공 시 회원 정보를 세션에 저장
                session.setAttribute("loggedInUser", member);   //  전체 Member 객체 저장
                session.setAttribute("userId", member.getUserId());
                session.setAttribute("name", member.getName()); // 사용자 식별을 위해 지정
                session.setMaxInactiveInterval(1800);   // 세션 30분

                logger.info("User logged in: {}.   User ID: {}. Session ID : {}", member.getMId(), member.getUserId(), session.getId());

                response.put("status", "success");
                response.put("message", "로그인 성공");
                response.put("redirect", "/api/main");
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Login failed for user: {}.  Invalid credentials.", mId);
                response.put("status", "error");
                response.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            logger.warn("Error during login process for user: {}. Error : {}", mId, e.getMessage());
            response.put("status", "error");
            response.put("message", "로그인 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
}