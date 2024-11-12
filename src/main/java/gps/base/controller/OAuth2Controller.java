package gps.base.controller;

import gps.base.model.Member;
import gps.base.model.ProviderType;
import gps.base.repository.MemberRepository;
import gps.base.service.OAuth2Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);


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
                setMemberSession(session, member);

                return "redirect:/";
            } else {
                // RedirectAttributes를 사용하여 파라미터 전달
                attributes.addAttribute("provider", "KAKAO");
                attributes.addAttribute("kakaoId", result.get("providerId"));
                attributes.addAttribute("nickname", result.get("nickname"));
                attributes.addAttribute("profileImage", result.get("profileImage"));

                return "redirect:/api/register";  // 경로 수정
            }
        } catch (Exception e) {
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
    public String googleCallback(@RequestParam(required = false) String code, HttpSession session, RedirectAttributes attributes) {
        try {
            Map<String, String> result = oAuth2Service.googleLogin(code);
            logger.info("Received code: {}", code);




            String providerId = result.get("providerId");
            String nickname = result.get("name");
            String email = result.get("email");
            String profileImage = result.get("picture");

            Optional<Member> existingMember = memberRepository.findByProviderTypeAndProviderId(
                    ProviderType.GOOGLE, providerId);

            if (existingMember.isPresent()) {
                logger.info("Existing member found: {}", existingMember.get());
            } else {
                logger.info("No existing member found for providerId: {}", providerId);
            }

            if (existingMember.isPresent()) {
                Member member = existingMember.get();
                setMemberSession(session, member);
                return "redirect:/";
            } else {
                // RedirectAttributes를 사용하여 파라미터 전달
                attributes.addAttribute("provider", "GOOGLE");
                attributes.addAttribute("providerId", providerId);
                attributes.addAttribute("nickname", nickname); // Google의 name을 nickname으로 사용
                attributes.addAttribute("email", email);
                attributes.addAttribute("profileImage", profileImage); // Google의 picture를 profileImage로 사용

                return "redirect:/api/register";
            }
        } catch (Exception e) {
            return "redirect:/?error=google_login_failed";
        }
    }
}
