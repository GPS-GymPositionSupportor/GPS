package gps.base.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gps.base.DTO.GymDTO;
import gps.base.DTO.ImageDTO;
import gps.base.DTO.MemberDTO;
import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.*;
import gps.base.repository.ImageRepository;
import gps.base.service.GymService;
import gps.base.service.MemberService;
import gps.base.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/api")
public class MainController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GymService gymService;


    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    /*
    회원관리 
    
    1. 회원가입
    2. 로그인
    3. 프로필 이미지 업데이트

     */

    // 회원가입 폼
    @GetMapping("/register")
    public String showRegisterPage(
            @RequestParam(required = false) String provider,
            // 카카오 파라미터
            @RequestParam(required = false) String kakaoId,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String profileImage,
            // 구글 파라미터 추가
            @RequestParam(required = false) String googleId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            Model model
    ) {
        if (provider != null) {
            model.addAttribute("provider", provider);

            if ("KAKAO".equals(provider)) {
                model.addAttribute("kakaoId", kakaoId);
                model.addAttribute("nickname", nickname);
                model.addAttribute("profileImage", profileImage);
            }
            else if ("GOOGLE".equals(provider)) {
                model.addAttribute("googleId", googleId);
                model.addAttribute("name", name);
                model.addAttribute("email", email);
                model.addAttribute("profileImage", profileImage);
            }

            model.addAttribute("isSocialLogin", true);
        }

        return "register";
    }


    // 회원가입 데이터 POST
    @PostMapping("/register")
    public String registerMember(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("nickname") String nickname,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("gender") String gender,
            @RequestParam(value = "profileImage", required = false) String profileImage,
            @RequestParam(value = "privacy", required = false) boolean privacy,
            @RequestParam(value = "provider", defaultValue = "LOCAL") String provider, // 기본 값은 LOCAL
            @RequestParam(value = "kakaoId", required = false) String kakaoId,
            @RequestParam(value = "googleId", required = false) String googleId,
            @RequestParam(value = "providerId", required = false) String providerId,
            Model model
    ) {
        logger.info("회원 등록 요청을 받았습니다. ID: {}", username);

        try {
            if (!privacy) {
                throw new RuntimeException("개인정보 수집에 동의해주세요.");
            }

            Member member = new Member();
            member.setMId(username);
            member.setMPassword(password);
            member.setName(name);
            member.setEmail(email);
            member.setNickname(nickname);
            member.setGender(gender);
            member.setProfileImg(profileImage);

            logger.info("=== Register Request Parameters ===");
            logger.info("Provider: {}", provider);
            logger.info("Username: {}", username);
            logger.info("Profile Image: {}", profileImage);
            logger.info("KakaoId: {}", kakaoId);
            logger.info("GoogleId: {}", googleId);
            logger.info("Name: {}", name);
            logger.info("Email: {}", email);
            logger.info("Nickname: {}", nickname);

            // 생년월일 변환
            if (birthdate != null && !birthdate.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
                LocalDateTime birth = LocalDate.parse(birthdate, formatter)
                        .atStartOfDay();
                member.setBirth(birth);
            }

            // 제공자 타입에 따른 providerId 설정
            switch (provider.toUpperCase()) {
                case "KAKAO":
                    member.setProviderType(ProviderType.KAKAO);
                    member.setProviderId(kakaoId);
                    break;
                case "GOOGLE":
                    member.setProviderType(ProviderType.GOOGLE);
                    member.setProviderId(googleId);
                    logger.info("member.setProviderId(googleId) : {}", providerId);
                    break;
                default:
                    member.setProviderType(ProviderType.LOCAL);
                    break;
            }

            member.setAuthority(Authority.USER);

            Member savedMember = memberService.saveMember(member);

            logger.info("회원 등록 완료 : {}", savedMember.getMId());
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/api/login";

        } catch (Exception e) {
            logger.error("회원 등록 실패", e);
            model.addAttribute("error", "회원 등록 실패 : " + e.getMessage());
            return "register";
        }
    }

    // id 중복 체크
    @PostMapping("/check-id")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");

        if (username == null || username.trim().isEmpty()) {
            return Map.of("isDuplicate", false);  // username이 비어있거나 공백일 경우 바로 false 반환
        }

        boolean isDuplicate = memberService.existsBymId(username);

        return Map.of("isDuplicate", isDuplicate);
    }



    // 닉네임 중복 체크
    @PostMapping("/check-nickname")
    @ResponseBody
    public Map<String, Boolean> checkNickname(@RequestBody Map<String, String> requestBody) {
        String nickname = requestBody.get("nickname");

        if (nickname == null || nickname.trim().isEmpty()) {
            return Map.of("isDuplicate", false);  // 닉네임이 비어있거나 공백일 경우 바로 false 반환
        }

        boolean isDuplicate = memberService.existsByNickname(nickname);

        return Map.of("isDuplicate", isDuplicate);
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
            HttpSession session) {

        try {
            Member member = memberService.authenticateMember(username, password);

            // 로그인 실패
            if(member == null) {
                session.setAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
                return "redirect:/";
            }

            // 로그인 성공: 세션 설정
            session.setAttribute("loggedInUser", member);
            session.setAttribute("userID", member.getUserId());
            session.setAttribute("nickname", member.getNickname());
            session.setAttribute("name", member.getName());
            session.setAttribute("authority", member.getAuthority());
            session.setAttribute("profile_img", member.getProfileImg());
            session.setAttribute("gender", member.getGender());
            session.setMaxInactiveInterval(1800);

            // 권한 체크 및 리다이렉션
            String authority = String.valueOf(member.getAuthority());
            if ("ADMIN".equals(authority)) {
                return "redirect:/api/admin";
            }
            return "redirect:/api/home";

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
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");


        try {
            if (loggedInUser != null) {
                logger.info("User logged out: {}. Session ID : {}", loggedInUser.getMId(), session.getId());

                // 세션에 설정된 모든 속성 제거
                session.removeAttribute("loggedInUser");
                session.removeAttribute("userID");
                session.removeAttribute("nickname");
                session.removeAttribute("name");
                session.removeAttribute("authority");

                // 세션 무효화
                session.invalidate();
                return "redirect:/api/login";

            } else {
                logger.warn("Logout attempted with no user in session. Session ID : {}", session.getId());
                loggedInUser.updateLastLogin(LocalDateTime.now());
                return "redirect:/api/login";
            }
        } catch (Exception e) {
            logger.error("Error during logout: ", e);
            return "redirect:/";
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
        return "home";
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
            userInfo.put("userID", String.valueOf(loggedInUser.getUserId()));
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
        if (session.getAttribute("userID") == null) {
            return "redirect:/api/login";
        } else {
            return "admin";
        }

    }




    /*
    체육관 관리
     */

    // HTMl 호출
    @GetMapping("/showgyms")
    @ResponseBody
    public Map<String, Object> getGyms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            HttpSession session) {
        try {
            if (session.getAttribute("loggedInUser") == null) {
                throw new  CustomException(ErrorCode.MEMBER_NOT_FOUND);
            }

            Map<String, Object> response = new HashMap<>();

            Page<Gym> gymPage = gymService.getGyms(PageRequest.of(page, size));
            Page<GymDTO> gymDTOPage = gymPage.map(gym -> {
                GymDTO dto = convertToDTO(gym);
                Optional<Image> image = imageRepository.findFirstByGymId(gym.getGymId());
                image.ifPresent(img -> dto.setGymImage(ImageDTO.fromEntity(img)));
                response.put("imageUrl", image.get().getImageUrl());
                return dto;
            });


            response.put("content", gymDTOPage.getContent());
            response.put("totalElements", gymDTOPage.getTotalElements());
            response.put("totalPages", gymDTOPage.getTotalPages());
            response.put("currentPage", gymDTOPage.getNumber());

            return response;
        } catch (CustomException e) {
            log.error("Error retrieving gyms: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }



    private GymDTO convertToDTO(Gym gym) {
        GymDTO dto = new GymDTO();
        dto.setGymId(gym.getGymId());
        dto.setAddress(gym.getAddress());
        dto.setGName(gym.getGName());
        dto.setOpenHour(gym.getOpenHour());
        dto.setHomepage(gym.getHomepage());
        dto.setPhone(gym.getPhone());
        dto.setGCreatedBy(gym.getGCreatedBy());
        dto.setGCreatedAt(gym.getGCreatedAt());
        dto.setRating(gym.getRating());
        return dto;
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

    // 체육관 삭제
    @DeleteMapping("/{gymId}")
    public ResponseEntity<?> deleteGym(@PathVariable Long gymId, Long userId , HttpSession session) {
        Long sessionId = (Long) session.getAttribute("userID");
        Authority authority = (Authority) session.getAttribute("authority");

        if (sessionId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        gymService.validateGymAndImage(gymId, userId, null, authority);
        gymService.deleteGym(gymId, userId, authority);

        return ResponseEntity.ok().build();
    }

    // 체육관 수정
    @PutMapping("/gyms/{gymId}")
    @ResponseBody
    public ResponseEntity<GymDTO> updateGym(
            @PathVariable Long gymId,
            @RequestBody GymDTO gymData,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            GymDTO updatedGym = gymService.updateGym(gymId, gymData);
            return ResponseEntity.ok(updatedGym);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }



}
