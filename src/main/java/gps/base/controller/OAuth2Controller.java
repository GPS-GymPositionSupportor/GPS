package gps.base.controller;

import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Member;
import gps.base.model.ProviderType;
import gps.base.repository.MemberRepository;
import gps.base.service.OAuth2Service;
import gps.base.service.TokenBasedSSOService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final TokenBasedSSOService ssoService;
    private final MemberRepository memberRepository;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 카카오 로그인 콜백 처리
     * @param code 카카오에서 받은 인증 코드
     * @return JWT 토큰을 포함한 응답
     */
    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code, HttpSession session, RedirectAttributes attributes) {
        try {
            Map<String, String> result = oAuth2Service.kakaoLogin(code);

            Optional<Member> existingMember = memberRepository.findByProviderTypeAndProviderId(
                    ProviderType.KAKAO,
                    result.get("providerId")
            );

            Map<String, Object> response = new HashMap<>();

            if (existingMember.isPresent()) {
                Member member = existingMember.get();
                setupSSOAndSession(session, member);
                logger.info("member : {} ", existingMember.get());

                response.put("status", "success");
                response.put("redirectUrl", "/");
                String ssoToken = ssoService.createSSOToken(member);
                response.put("ssoToken", ssoToken);

                return ResponseEntity.ok(response);
            } else {
                response.put("status", "registration_required");
                response.put("kakaoId", result.get("providerId"));
                response.put("nickname", result.get("nickname"));
                response.put("profileImage", result.get("profileImage"));

                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Kakao login failed", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "kakao_login_failed");
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    private void setMemberSession(HttpSession session, Member member) {
        session.setAttribute("loggedInUser", member);
        session.setAttribute("userID", member.getUserId());
        session.setAttribute("nickname", member.getNickname());
        session.setAttribute("name", member.getName());
        session.setAttribute("authority", member.getAuthority());
        session.setMaxInactiveInterval(1800);
    }

    /**
     * 구글 로그인 콜백 처리
     * @param code 구글에서 받은 인증 코드
     * @return JWT 토큰을 포함한 응답
     */
    @GetMapping("/google")
    public String googleCallback(@RequestParam String code, HttpSession session, RedirectAttributes attributes) {
        try {
            Map<String, String> result = oAuth2Service.googleLogin(code);


            Optional<Member> existingMember = memberRepository.findByProviderTypeAndProviderId(
                    ProviderType.GOOGLE,
                    result.get("providerId")
            );

            if (code == null) {
                log.error("No code parameter received");
                return "redirect:/?error=no_code";
            }

            if (existingMember.isPresent()) {
                Member member = existingMember.get();
                setupSSOAndSession(session,member);
                return "redirect:/";  // 성공적으로 로그인 후 메인 페이지로 리디렉션
            } else {

                attributes.addAttribute("provider", "GOOGLE");
                attributes.addAttribute("googleId", result.get("googleId"));
                attributes.addAttribute("nickname", result.get("nickname"));
                attributes.addAttribute("email", result.get("email"));
                attributes.addAttribute("profileImage", result.get("profileImage"));

                // 로깅 추가
                log.info("Redirecting to /api/register with attributes:");
                log.info("Provider: {}", "GOOGLE");
                log.info("Google ID: {}", result.get("googleId"));
                log.info("Nickname: {}", result.get("nickname"));
                log.info("Email: {}", result.get("email"));
                log.info("Profile Image: {}", result.get("profileImage"));

                return "redirect:/api/register";  // 신규 회원 등록 페이지로 리디렉션
            }
        } catch (Exception e) {
            return "redirect:/?error=google_login_failed";  // 로그인 실패 시 에러 페이지로 리디렉션
        }
    }

    @GetMapping("/google/url")
    @ResponseBody
    public String getGoogleAuthUrl() {
        log.info("Generating Google auth URL");

        String redirectUri = "http://localhost:8080/auth/google";
        String scope = "email profile openid";

        String url = String.format("https://accounts.google.com/o/oauth2/v2/auth"
                        + "?client_id=%s"
                        + "&redirect_uri=%s"
                        + "&response_type=code"
                        + "&scope=%s",
                googleClientId,
                URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                URLEncoder.encode(scope, StandardCharsets.UTF_8));

        log.info("Generated Google auth URL: {}", url);
        return url;
    }


    /**
     * SSO 토큰 생성 및 세션 설정을 처리하는 통합 메서드
     */
    private void setupSSOAndSession(HttpSession session, Member member) {
        // SSO 토큰 생성
        String ssoToken = ssoService.createSSOToken(member);

        // 세션에 SSO 토큰 저장
        session.setAttribute("ssoToken", ssoToken);

        // 기존 세션 정보도 함께 저장
        session.setAttribute("loggedInUser", member);
        session.setAttribute("userID", member.getUserId());
        session.setAttribute("nickname", member.getNickname());
        session.setAttribute("name", member.getName());
        session.setAttribute("authority", member.getAuthority());

        // 세션 만료 시간 설정 (30분)
        session.setMaxInactiveInterval(1800);
    }

    /**
     * SSO 로그아웃 처리
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("ssoToken");
        session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/validate-token")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validateToken(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String ssoToken = (String) session.getAttribute("ssoToken");

        try {
            if (ssoToken != null) {
                Claims claims = ssoService.validateSSOToken(ssoToken);
                if (!claims.getExpiration().before(new Date())) {
                    response.put("authenticated", true);
                    response.put("userId", claims.get("userID"));
                    response.put("expiresAt", claims.getExpiration());
                    return ResponseEntity.ok(response);
                }
            }
        } catch (Exception e) {
            logger.error("Token validation failed", e);
        }

        response.put("authenticated", false);
        return ResponseEntity.ok(response);
    }


}
