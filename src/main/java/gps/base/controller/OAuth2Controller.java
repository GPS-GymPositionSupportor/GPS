package gps.base.controller;

import gps.base.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;


    /**
     * 카카오 로그인 콜백 처리
     * @param code 카카오에서 받은 인증 코드
     * @return JWT 토큰을 포함한 응답
     */
    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(code));
    }

    /**
     * 구글 로그인 콜백 처리
     * @param code 구글에서 받은 인증 코드
     * @return JWT 토큰을 포함한 응답
     */
    @GetMapping("/google")
    public ResponseEntity<?> googleCallback(@RequestParam String code) {
        return ResponseEntity.ok(oAuth2Service.googleLogin(code));
    }
}
