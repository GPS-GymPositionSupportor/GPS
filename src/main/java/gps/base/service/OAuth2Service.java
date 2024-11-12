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

            // 1. 액세스 토큰 받기 (구글의 OAuth2 API에 인증 코드로 요청)
            String accessToken = getGoogleAccessToken(code);
            log.info("Access token received successfully: {}", accessToken);

            // 2. 액세스 토큰을 사용하여 사용자 정보 가져오기
            Map<String, Object> userInfo = getGoogleUserInfo(accessToken);
            log.info("User info received: {}", userInfo);

            // 3. 필요한 정보 추출
            String providerId = String.valueOf((userInfo.get("id")));  // Google의 고유 ID
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");
            String picture = (String) userInfo.get("picture");

            // 4. 결과 매핑
            Map<String, String> result = new HashMap<>();
            result.put("providerId", providerId);
            result.put("googleId", providerId);

            result.put("email", email);
            result.put("name", name);
            result.put("profileImage", picture);

            log.info("providerId = {}", providerId);


            log.info("Google login process completed successfully");
            return result;

        } catch (Exception e) {
            log.error("Error in googleLogin", e);
            throw new RuntimeException("Failed to process Google login: " + e.getMessage(), e);
        }
    }

    /**
     * 구글 액세스 토큰 받기
     * @param code 인증 코드
     * @return 구글 액세스 토큰
     */
    private String getGoogleAccessToken(String code) {
        log.info("Getting Google access token...");
        String url = "https://oauth2.googleapis.com/token";

        try {
            // 구글 토큰 요청에 필요한 파라미터 설정
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");  // 정확한 값으로 설정
            params.add("client_id", googleClientId);
            params.add("client_secret", googleClientSecret);
            params.add("code", code);
            params.add("redirect_uri", googleRedirectUri);  // 정확한 redirect_uri

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // Content-Type 설정

            // HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(params, headers);

            log.info("Token request params: {}", params);  // 파라미터 로깅

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    url,
                    request,
                    Map.class
            );

            log.info("Google token response status: {}", response.getStatusCode());

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("access_token")) {
                return (String) body.get("access_token");
            } else {
                log.error("No access token in response: {}", body);
                throw new RuntimeException("No access token in response");
            }

        } catch (Exception e) {
            log.error("Error getting Google access token: {}", e.getMessage());
            throw new RuntimeException("Failed to get Google access token", e);
        }
    }

    /**
     * 구글 사용자 정보 가져오기
     * @param accessToken 구글 액세스 토큰
     * @return 구글 사용자 정보
     */
    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        // 액세스 토큰을 사용하여 구글 사용자 정보를 받는 코드
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);
        return response.getBody();
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
