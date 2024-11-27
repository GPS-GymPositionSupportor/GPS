package gps.base.controller;

import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Member;
import gps.base.model.ProviderType;
import gps.base.repository.MemberRepository;
import gps.base.service.OAuth2Service;
import gps.base.service.TokenBasedSSOService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate redisTemplate;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 카카오 로그인 콜백 처리
     * @param code 카카오에서 받은 인증 코드
     * @return JWT 토큰을 포함한 응답
     */
    @GetMapping("/kakao")
    public String kakaoCallback(@RequestParam String code, HttpSession session, RedirectAttributes attributes) {
        try {
            Map<String, String> result = oAuth2Service.kakaoLogin(code);

            Optional<Member> existingMember = memberRepository.findByProviderTypeAndProviderId(
                    ProviderType.KAKAO,
                    result.get("providerId")
            );

            if (existingMember.isPresent()) {
                Member member = existingMember.get();
                setupSSOAndSession(session, member);
                logger.info("member : {} ", existingMember.get());

                // SSO 토큰 생성 및 세션에 저장
                String ssoToken = ssoService.createSSOToken(member);
                session.setAttribute("ssoToken", ssoToken);

                return "redirect:/api/main";
            } else {
                // RedirectAttributes를 사용하여 파라미터 전달
                attributes.addAttribute("provider", "KAKAO");
                attributes.addAttribute("kakaoId", result.get("providerId"));
                attributes.addAttribute("nickname", result.get("nickname"));
                attributes.addAttribute("profileImage", result.get("profileImage"));

                return "redirect:/api/register";  // 회원가입 페이지로 리다이렉트
            }
        } catch (Exception e) {
            logger.error("Kakao login failed", e);
            return "redirect:/?error=kakao_login_failed";
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

            if (existingMember.isPresent()) {
                Member member = existingMember.get();
                setupSSOAndSession(session, member);
                return "redirect:/api/main";
            } else {
                attributes.addAttribute("provider", "GOOGLE");
                attributes.addAttribute("googleId", result.get("providerId"));
                attributes.addAttribute("nickname", result.get("nickname"));
                attributes.addAttribute("email", result.get("email"));
                attributes.addAttribute("profileImage", result.get("profileImage"));
                return "redirect:/api/register";
            }
        } catch (Exception e) {
            return "redirect:/?error=google_login_failed";
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
    private String setupSSOAndSession(HttpSession session, Member member) {
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
        session.setMaxInactiveInterval(1800);

        return ssoToken;
    }

    /**
     * SSO 로그아웃 처리
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loggedInUser");
        if (member != null) {
            // Redis에서 토큰 삭제
            String redisKey = "sso:token:" + member.getUserId();
            redisTemplate.delete(redisKey);
            logger.info("Removed SSO token for user: {}", member.getUserId());
        }

        // 세션 무효화
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
                response.put("authenticated", true);
                response.put("userID", claims.get("userID"));
                response.put("ssoToken", ssoToken);  // 토큰을 응답에 포함
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Token validation failed", e);
        }

        response.put("authenticated", false);
        return ResponseEntity.ok(response);
    }


}
