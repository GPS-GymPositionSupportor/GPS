package gps.base.service;

import gps.base.DTO.*;
import gps.base.model.Authority;
import gps.base.model.Member;
import gps.base.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 어느 플랫폼에서 온 id 인지 확인

        Oauth2Response oauth2Response = null;

        if (registrationId.equals("kakao")) {
            oauth2Response = new KaKaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oauth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            //sso 연동 로그인이 아닐때
            return null;
        }

        String mId = oauth2Response.getProvider() + " " + oauth2Response.getProviderId();

        Member existData = memberRepository.findBymId(mId).orElse(null);

        if (existData == null) { // 유저 정보가 존재 하지 않는경우
            //유저 정보 저장
            Member member = new Member();
            member.setMId(mId);
            member.setEmail(oauth2Response.getEmail());
            member.setName(oauth2Response.getName());
            member.setAuthority(Authority.USER);

            memberRepository.save(member);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(mId);
            userDTO.setName(oauth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        } else {                   // 유저 정보가 존재 하는 경우
            existData.setEmail(oauth2Response.getEmail());
            existData.setName(oauth2Response.getName());

            memberRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getMId());
            userDTO.setName(existData.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
    }
}
