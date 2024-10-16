package gps.base.service;

import gps.base.model.Member;
import gps.base.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService {

    // application.properties 에서 설정
    //@Value("${upload.path}") <- 설정 한후에 주석 제거하셈
    private String uploadPath;

    //@Autowired
    //private PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public Member saveMember(Member member) {
        if (member.getMPassword() == null || member.getMPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력 항목입니다.");
        }

        try {
            member.setMPassword(member.getMPassword());
            member.setMCreatedAt(LocalDateTime.now());
            return memberRepository.save(member);
        } catch (Exception e) {
            throw new RuntimeException("회원 저장 중 오류 발생 : " + e.getMessage(), e);
        }
    }


    public Member getMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. ID : " + userId));
    }

    public Member getMemberBymId(String mId) {
        return memberRepository.findBymId(mId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. ID : " + mId));
    }


    // 로그인 허가 메소드
    public Member authenticateMember(String mId, String mPassword) {

        try {
            Member member = memberRepository.findBymId(mId)
                    .orElse(null);


            if (member != null && mPassword.equals((member.getMPassword()))) {
                return member;
            }
            return null;
        } catch (EntityNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            return null;
        }
    }
}