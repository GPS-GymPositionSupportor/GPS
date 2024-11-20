package gps.base.service;

import gps.base.DTO.MemberDTO;
import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Admin;
import gps.base.model.Authority;
import gps.base.model.Member;
import gps.base.repository.AdminRepository;
import gps.base.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 환경 변수로 설정
    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Autowired
    AdminRepository adminRepository;


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;



    @Transactional
    public Member saveMember(Member member) {
        if (member.getMPassword() == null || member.getMPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력 항목입니다.");
        }

        try {
            member.setMPassword(passwordEncoder.encode(member.getMPassword()));
            member.setMCreatedAt(LocalDateTime.now());
            return memberRepository.save(member);
        } catch (Exception e) {
            throw new RuntimeException("회원 저장 중 오류 발생 : " + e.getMessage(), e);
        }
    }
    
    public void registerMember(Member member) {
        // 유효성 검사 가능

        memberRepository.save(member);
    }


    public Member getMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getMemberBymId(String mId) {
        return memberRepository.findBymId(mId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }


    // 로그인 허가 메소드
    public Member authenticateMember(String mId, String mPassword) {
        Member member = memberRepository.findBymId(mId)
                .orElse(null);

        if (member != null) {
            String storedPassword = member.getMPassword();

            // BCrypt로 암호화된 비밀번호는 항상 $2a$, $2b$ 또는 $2y$로 시작
            boolean isEncrypted = storedPassword.startsWith("$2a$") ||
                    storedPassword.startsWith("$2b$") ||
                    storedPassword.startsWith("$2y$");

            if (isEncrypted) {
                // 암호화된 비밀번호인 경우
                if (passwordEncoder.matches(mPassword, storedPassword)) {
                    return member;
                }
            } else {
                // 평문 비밀번호인 경우
                if (mPassword.equals(storedPassword)) {
                    // 로그인 성공 시 비밀번호를 암호화하여 업데이트
                    member.setMPassword(passwordEncoder.encode(mPassword));
                    memberRepository.save(member);
                    return member;
                }
            }
        }
        return null;
    }


    // 프로필 정보 업데이트 (birth는 수정 불가로 가정)
    public void updateProfileInfo(Long userId, MemberDTO profileData) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (profileData.getNickname() != null) {
            member.setNickname(profileData.getNickname());
        }
        if (profileData.getEmail() != null) {
            member.setEmail(profileData.getEmail());
        }
        // birth는 수정하지 않음 (나이는 birth에서 자동 계산)

        memberRepository.save(member);
    }



    // 사용자 ID와 업로드 된 파일을 받아 프로필 이미지를 업데이트 하는 함수
    @Transactional
    public void updateProfileImage(Long userId, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // UUID.randomUUID() 를 통해 고유한 식별자를 생성하고, 원본 파일 이름을 붙여 파일명 충돌을 방지함.
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        // StandardCopyOption.REPLACE_EXISTING 은 같은 이름의 파일이 이미 존재할 경우 덮어쓰기 허용
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        member.setProfileImg("/uploads/" + fileName);   // 웹에서 접근 가능한 경로
        memberRepository.save(member);
    }


    // 회원 삭제 기능 (수정 필요)
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }

    // 마지막 접속시간 업데이트
    @Transactional
    public void updateLastLogin(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateLastLogin(LocalDateTime.now());
    }

    public Page<MemberDTO> getAllMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(MemberDTO::from);
    }

    @Transactional
    public void updateAuthority(Long memberId, Authority newAuthority) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // ADMIN으로 권한 변경 시 admin 테이블에도 데이터 추가
        if(newAuthority == Authority.ADMIN) {
            Admin admin = new Admin();
            admin.setUserId(memberId);
            admin.setAId(member.getEmail());      // member의 email을 admin의 a_id로 사용
            admin.setAPassword(member.getMPassword());  // member의 password를 admin의 a_password로 사용
            adminRepository.save(admin);
        }

        member.updateAuthority(newAuthority);
    }





    public boolean existsBymId(String mId) {
        return memberRepository.existsBymId(mId);
    }

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public void updateMember(Long userId, MemberDTO memberDTO) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getNickname().equals(memberDTO.getNickname())
                && memberRepository.existsByNickname(memberDTO.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER_NICKNAME);
        }

        member.updateInfo(
                memberDTO.getName(),
                memberDTO.getNickname(),
                memberDTO.getEmail(),
                memberDTO.getBirth(),
                memberDTO.getGender(),
                memberDTO.getAuthority()
        );
    }
}
