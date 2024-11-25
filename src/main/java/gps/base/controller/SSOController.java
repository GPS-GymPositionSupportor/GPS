package gps.base.controller;


import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.service.OAuth2Service;
import gps.base.service.TokenBasedSSOService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sso")
@RequiredArgsConstructor
public class SSOController {

    private final TokenBasedSSOService ssoService;
    private final HttpSession session;

    @GetMapping("/validate")
    public Map<String, Object> validateSSO(@RequestParam String token) {
        Claims claims = ssoService.validateSSOToken(token);
        return claims;
    }

    @GetMapping("/login")
    public String handleSSOLogin(@RequestParam String targetUrl) {
        // SSO 토큰이 있는 경우 taretURL 로 리다이렉트
        if (isValidSSOToken()) {
            return "redirect:" + targetUrl + "?ssoToken=" + getCurrentSSOToken();
        }
        // 없는 경우 카카오 / 구글 로그인 페이지로 리다이렉트
        return "redirect:/oauth2/authorization/kakao";
    }


    // SSO 상태 확인 엔드포인트 추가
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkSSOStatus() {
        Map<String, Object> response = new HashMap<>();

        if (isValidSSOToken()) {
            String token = getCurrentSSOToken();
            Claims claims = ssoService.validateSSOToken(token);

            response.put("authenticated", true);
            response.put("userId", claims.get("userId"));
            response.put("expiresAt", claims.getExpiration());

            return ResponseEntity.ok(response);
        }

        response.put("authenticated", false);
        return ResponseEntity.ok(response);
    }

    // SSO 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        session.removeAttribute("ssoToken");
        return ResponseEntity.ok().build();
    }

    // 에러 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleError(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }



    // SSO 토큰 유효성 검증
    private boolean isValidSSOToken() {
        String ssoToken = (String) session.getAttribute("ssoToken");
        if (ssoToken == null) {
            return false;
        }
        try {
            Claims claims = ssoService.validateSSOToken(ssoToken);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 현재 SSO 토큰 가져오기
    private String getCurrentSSOToken() {
        return (String) session.getAttribute("ssoToken");
    }
}
