package gps.base.service;

import gps.base.model.Member;
import gps.base.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MemberService {

    // application.properties 에서 설정
    //@Value("${upload.path}") <- 설정 한후에 주석 제거하셈
    private String uploadPath;


    private PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;



    @Transactional
    public Member saveMember(Member member) {
        member.setMPassword(passwordEncoder.encode(member.getMPassword()));
        member.setMCreatedAt(LocalDateTime.now());
        return memberRepository.save(member);
    }


    public Member getMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. ID : " + userId));
    }


    // 로그인 허가 메소드
    public boolean authenticateMember(String mId, String mPassword) {
        Member member = memberRepository.findBymId(mId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. ID : " + mId));

        return passwordEncoder.matches(mPassword, member.getMPassword());
    }


    // 사용자 ID와 업로드 된 파일을 받아 프로필 이미지를 업데이트 하는 함수
    @Transactional
    public void updateProfileImage(Long userId, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        // UUID.randomUUID() 를 통해 고유한 식별자를 생성하고, 원본 파일 이름을 붙여 파일명 충돌을 방지함.
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        // StandardCopyOption.REPLACE_EXISTING 은 같은 이름의 파일이 이미 존재할 경우 덮어쓰기 허용
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        member.setProfileImg("/uploads/" + fileName);   // 웹에서 접근 가능한 경로
        memberRepository.save(member);
    }


    // 회원 삭제 기능
    @Transactional
    public void deleteMember(Long userId) {
        Member member = getMemberById(userId);
        member.setMDeletedAt(LocalDateTime.now());
        member.setMDeletedBv(userId);
        memberRepository.save(member);
    }
}
