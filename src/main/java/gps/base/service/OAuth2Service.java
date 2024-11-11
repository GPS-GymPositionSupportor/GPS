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
     * 소셜 로그인 사용자 정보 처리
     * @param userInfo 소셜 플랫폼에서 받은 사용자 정보
     * @param provider 소셜 플랫폼 제공자(KAKAO, GOOGLE 등)
     * @return 생성되거나 조회된 Member 엔티티
     */
    private Member processMemberInfo(Map<String, Object> userInfo, String provider) {
        // 카카오 계정 정보와 프로필 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // providerId 를 기본 식별자로 사용
        String providerId = userInfo.get("id").toString();
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        // 이메일은 옵셔널하게 처리
        String email = kakaoAccount.get("email") != null ?
                (String) kakaoAccount.get("email") :
                "kakao_" + providerId + "@gps.com"; // 이메일이 없을 경우 대체 값


        // providerId로 회원 조회
        Optional<Member> existingMember = memberRepository.findByProviderIdAndProviderType(
                ProviderType.KAKAO,
                providerId
        );

        if(existingMember.isPresent()) {
            Member member = existingMember.get();
            // 기존 회원 정보 업데이트
            member.setNickname(nickname);
            member.setProfileImg(profileImageUrl);
            return memberRepository.save(member);
        }


        // 이메일로 한번 더 체크 (이메일이 있는 경우에만)
        if (kakaoAccount.get("email") != null) {
            Optional<Member> memberByEmail = memberRepository.findByEmail(email);
            if (memberByEmail.isPresent()) {
                // 일반 회원가입 사용자가 카카오 로그인을 시도하는 경우
                if (memberByEmail.get().getProviderType() == ProviderType.LOCAL) {
                    throw new RuntimeException(
                            "이미 일반 회원으로 가입된 이메일입니다. 일반 로그인을 이용해주세요."
                    );
                }
            }
        }
        
        // 신규 회원가입
        Member newMember = new Member();
        newMember.setEmail(email);
        newMember.setName(nickname);
        newMember.setProfileImg(profileImageUrl);
        newMember.setProviderType(ProviderType.valueOf(providerId));
        newMember.setProviderId(providerId);

        // 소셜 로그인용 임의 ID/PW 생성
        newMember.setMId(generateSocialMemberId(provider, providerId));
        newMember.setMPassword(passwordEncoder.encode(generateRandomPassword()));
        newMember.setAuthority(Authority.USER);

        return memberRepository.save(newMember);
    }



    /**
     * 구글 로그인 처리
     * @param code 구글 인증 코드
     * @return JWT 토큰이 포함된 Map
     */
    public Map<String, String> googleLogin(String code) {
        // 1. 구글 액세스 토큰 받기
        String accessToken = getGoogleAccessToken(code);

        // 2. 액세스 토큰으로 구글 사용자 정보 가져오기
        Map<String, Object> userInfo = getGoogleUserInfo(accessToken);

        // 3. 구글 사용자 정보로 회원가입/로그인 처리
        Member member = processGoogleMemberInfo(userInfo);

        // 4. JWT 토큰 생성하여 반환
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getAuthority().toString());

        return Map.of("token", token);

    }



    /**
     * 구글 액세스 토큰 받기
     * @param code 인증 코드
     * @return 구글 액세스 토큰
     */
    private String getGoogleAccessToken(String code) {
        String url = "https://oauth2.googleapis.com/token";

        // 구글 토큰 요청에 필요한 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/auth/google");

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            // 구글 서버로 POST 요청
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            logger.error("Failed to get Google access token", e);
            throw new RuntimeException("Failed to get Google access token", e);
        }
    }


    /**
     * 구글 사용자 정보 가져오기
     * @param accessToken 구글 액세스 토큰
     * @return 구글 사용자 정보
     */
    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        // Authorization 헤더에 액세스 토큰 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            // 구글 API로 GET 요청
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );
            return response.getBody();
        } catch (Exception e) {
            logger.error("Failed to get Google user info", e);
            throw new RuntimeException("Failed to get Google user info", e);
        }
    }



    /**
     * 구글 사용자 정보 처리
     * @param userInfo 구글에서 받은 사용자 정보
     * @return 생성되거나 조회된 Member 엔티티
     */
    private Member processGoogleMemberInfo(Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String pictureUrl = (String) userInfo.get("picture");

        // 이메일로 회원 조회 후 없으면 새로 생성
        return memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    Member newMember = new Member();
                    newMember.setEmail(email);
                    newMember.setName(name);
                    newMember.setNickname(name);  // 구글은 별도 닉네임이 없어서 name으로 설정
                    newMember.setProfileImg(pictureUrl);
                    newMember.setProviderType(ProviderType.GOOGLE);
                    newMember.setProviderId(userInfo.get("id").toString());
                    newMember.setAuthority(Authority.USER);
                    return memberRepository.save(newMember);
                });
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


    // 소셜 로그인용 회원 ID 생성
    private String generateSocialMemberId(String provider, String providerId) {
        // 예: kakao_1234567 또는 google_abc123def 형식으로 생성
        return provider.toLowerCase() + "_" + providerId;
    }

    // 임의의 비밀번호 생성
    private String generateRandomPassword() {
        // UUID를 사용하여 랜덤한 문자열 생성
        return UUID.randomUUID().toString();
    }
}
