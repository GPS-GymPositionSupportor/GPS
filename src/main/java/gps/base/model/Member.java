package gps.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
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

    @Column(name = "profile_img", nullable = true)
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

    @Column(name = "m_deleted-at")
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

    // 나이 계산을 위한 메서드 추가
    public int calculateAge() {
        if (this.birth == null) {
            return 0;
        }
        return Period.between(this.birth.toLocalDate(), LocalDate.now()).getYears();
    }


    // 소셜 로그인 사용자용 비밀번호 생성
    public void generateRandomPassword() {
        // UUID를 사용하여 랜덤 비밀번호 생성
        this.mPassword = UUID.randomUUID().toString();
    }

    // 소셜 로그인 사용자용 mId 생성
    public void generateSocialId() {
        // 프로바이더 타입 + 프로바이더 ID를 조합하여 고유한 mId 생성
        this.mId = this.providerType.toString().toLowerCase() + "_" + this.providerId;
    }


}
