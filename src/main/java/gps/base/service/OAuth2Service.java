package gps.base.service;

import gps.base.component.JwtTokenProvider;
import gps.base.controller.MainController;
import gps.base.model.Authority;
import gps.base.model.Member;
import gps.base.model.ProviderType;
import gps.base.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Service.class);


    // OAuth2 클라이언트 정보
    @Value("${KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String kakaoClientSecret;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String googleRedirectUri;


    /**
     * 카카오 로그인 처리
     * @param code 카카오 인증 코드
     * @return JWT 토큰이 포함된 Map
     */
    public Map<String, String> kakaoLogin(String code) {
        try {
            log.info("Starting Kakao login process with code");

            // 1. 액세스 토큰 받기
            String accessToken = getKakaoAccessToken(code);
            log.info("Access token received successfully");

            // 2. 사용자 정보 받기
            Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
            log.info("User info received: {}", userInfo);

            // 3. 필요한 정보 추출
            String providerId = String.valueOf(userInfo.get("id"));
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
            Map<String, Object> profile = kakaoAccount != null ?
                    (Map<String, Object>) kakaoAccount.get("profile") : null;

            if (profile == null) {
                throw new RuntimeException("Failed to get profile information from Kakao");
            }

            // 4. 결과 매핑
            Map<String, String> result = new HashMap<>();
            result.put("providerId", providerId);
            result.put("kakaoId", providerId);
            result.put("nickname", (String) profile.get("nickname"));
            result.put("email", kakaoAccount.get("email") != null ?
                    (String) kakaoAccount.get("email") :
                    "kakao_" + providerId + "@placeholder.com");
            result.put("profileImage", (String) profile.get("profile_image_url"));

            log.info("Kakao login process completed successfully");
            return result;

        } catch (Exception e) {
            log.error("Error in kakaoLogin", e);
            throw new RuntimeException("Failed to process Kakao login: " + e.getMessage(), e);
        }
    }



    /**
     * 카카오 액세스 토큰 받기
     * @param code 인증 코드
     * @return 카카오 액세스 토큰
     */
    private String getKakaoAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        // 카카오 토큰 요청에 필요한 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/auth/kakao");

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 카카오 서버로 POST 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("access_token");
    }



    /**
     * 카카오 사용자 정보 가져오기
     * @param accessToken 카카오 액세스 토큰
     * @return 카카오 사용자 정보
     */
    private Map<String, Object> getKakaoUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        // Authorization 헤더에 액세스 토큰 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // 카카오 API로 GET 요청
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                Map.class
        );

        return response.getBody();
    }



    /**
     * 구글 로그인 처리
     * @param code 구글 인증 코드
     * @return JWT 토큰이 포함된 Map
     */
    public Map<String, String> googleLogin(String code) {
        try {
            log.info("Starting Google login process with code: {}", code);

            // 1. 액세스 토큰 받기
            String accessToken = getGoogleAccessToken(code);
            log.info("Successfully got access token");

            // 2. 사용자 정보 가져오기
            Map<String, Object> userInfo = getGoogleUserInfo(accessToken);
            log.info("Successfully got user info");

            // 3. 결과 매핑
            Map<String, String> result = new HashMap<>();
            result.put("providerId", String.valueOf(userInfo.get("id")));
            result.put("nickname", (String) userInfo.get("name"));
            result.put("email", (String) userInfo.get("email"));
            result.put("profileImage", (String) userInfo.get("picture"));

            log.info("Completed Google login process for user: {}", result.get("email"));
            return result;

        } catch (Exception e) {
            log.error("Failed to process Google login", e);
            throw new RuntimeException("Failed to process Google login", e);
        }
    }

    /**
     * 구글 액세스 토큰 받기
     * @param code 인증 코드
     * @return 구글 액세스 토큰
     */
    private String getGoogleAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("code", code);
        params.add("redirect_uri", googleRedirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        log.debug("Requesting access token with params: {}", params);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            log.debug("Token response status: {}", response.getStatusCode());

            if (response.getBody() != null && response.getBody().containsKey("access_token")) {
                return (String) response.getBody().get("access_token");
            }

            log.error("Token response body: {}", response.getBody());
            throw new RuntimeException("Failed to get access token");
        } catch (Exception e) {
            log.error("Error getting access token", e);
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    /**
     * 구글 사용자 정보 가져오기
     * @param accessToken 구글 액세스 토큰
     * @return 구글 사용자 정보
     */
    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getBody() != null) {
                return response.getBody();
            }

            throw new RuntimeException("Empty response from Google user info endpoint");
        } catch (Exception e) {
            log.error("Error getting user info", e);
            throw new RuntimeException("Failed to get user info", e);
        }
    }



    /**
     * 오류 응답을 처리하기 위한 DTO
     */
    @Getter
    @Setter
    public static class OAuthError {
        private String error;
        private String error_description;
    }

}
