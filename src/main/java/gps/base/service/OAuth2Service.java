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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

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
        // 1. 카카오 액세스 토큰 받기
        String accessToken = getKakaoAccessToken(code);

        // 2. 액세스 토큰으로 카카오 사용자 정보 가져오기
        Map<String, Object> userInfo = getKakaoUserInfo(accessToken);

        // 3. 카카오 사용자 정보로 회원가입/로그인 처리
        Member member = processMemberInfo(userInfo, "KAKAO");

        // 4. JWT 토큰 생성하여 반환
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getAuthority().toString());

        return Map.of("token", token);
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

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        // 이메일로 회원 조회 후 없으면 새로 생성
        return memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    Member newMember = new Member();
                    newMember.setEmail(email);
                    newMember.setName(nickname);
                    newMember.setNickname(nickname);
                    newMember.setProfileImg(profileImageUrl);
                    newMember.setProviderType(ProviderType.valueOf(provider));
                    newMember.setProviderId(userInfo.get("id").toString());
                    newMember.setAuthority(Authority.USER);
                    return memberRepository.save(newMember);
                });
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

}
