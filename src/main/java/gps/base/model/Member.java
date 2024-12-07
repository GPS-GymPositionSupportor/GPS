package gps.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Slf4j
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "m_id", nullable = false)
    private String mId;

    @Column(name = "m_password", nullable = false)
    private String mPassword;

    @Column(name = "name", nullable = false)
    private String name;

    /* String 값으로 프로필 이미지를 받는 이유.
        데이터베이스에 직접 이미지 파일 저장하는것 권장 X 
        이미지 파일은 서버의 File System 이나, 클라우드 스토리지에 저장 
        
        또는
        
        이미지 파일의 경로나 URL을 저장하는데, 이 때 경로 or /url 이 문자열 이므로 String Type 으로 선언
     */

    @Column(name = "nickname", nullable = true)
    private String nickname;

    @Column(name = "profile_image", nullable = true)
    private String profileImg;

    @Column(name = "birth")
    private LocalDateTime birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    private Authority authority;

    @Column(name = "m_created_by")
    private String mCreatedBy;

    @Column(name = "m_deleted_by")
    private Long mDeletedBy;

    @Column(name = "m_created_at")
    private LocalDateTime mCreatedAt;

    @Column(name = "m_deleted_at")
    private LocalDateTime mDeletedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;


    /*
    SSO 구현을 위한 변수
     */
    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;  // 소셜 로그인 제공자 타입

    @Column(name = "provider_id")
    private String providerId;  // 소셜 로그인 제공자의 사용자 ID


    
    
    // PrePersist 로 현재 시간 설정 또는 삭제
    @PrePersist
    protected void onCreate() {
        mCreatedAt = LocalDateTime.now();
        lastLogin = LocalDateTime.now();
    }

    @PreRemove
    protected void onRemove() {
        mDeletedAt = LocalDateTime.now();
    }

    public void updateLastLogin(LocalDateTime loginTime) {
        if (loginTime == null) {
            throw new IllegalArgumentException("로그인 시간이 null일 수 없습니다.");
        }

        // 이전 로그인 시간 저장 (필요한 경우)
        LocalDateTime previousLogin = this.lastLogin;

        // 새로운 로그인 시간 설정
        this.lastLogin = loginTime;

        // 로깅
        log.info("Member {} updated last login time from {} to {}",
                this.mId,
                previousLogin,
                loginTime);
    }

    public void updateAuthority(Authority authority) {
        if (authority == null) {
            throw new IllegalArgumentException("권한 값이 null일 수 없습니다.");
        }

        // ADMIN과 USER만 허용
        if (authority != Authority.ADMIN && authority != Authority.USER) {
            throw new IllegalArgumentException("유효하지 않은 권한입니다: " + authority);
        }

        this.authority = authority;
    }


    public void updateInfo(String name, String nickname, String email, LocalDateTime birth, String gender, Authority authority) {
        if (name != null) {
            this.name = name;
        }
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (email != null) {
            this.email = email;
        }
        if (birth != null) {
            this.birth = birth;
        }
        if (gender != null) {
            this.gender = gender;
        }
        if (authority != null) {
            this.authority = authority;
        }
    }
}
