package gps.base.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    @Column(name = "m_name", nullable = false)
    private String mName;

    /* String 값으로 프로필 이미지를 받는 이유.
        데이터베이스에 직접 이미지 파일 저장하는것 권장 X 
        이미지 파일은 서버의 File System 이나, 클라우드 스토리지에 저장 
        
        또는
        
        이미지 파일의 경로나 URL을 저장하는데, 이 때 경로 or /url 이 문자열 이므로 String Type 으로 선언
     */
    @Column(name = "profile_img", nullable = true)
    private String profileImg;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    private String authority;

    @Column(name = "m_created_by")
    private String mCreatedBy;

    @Column(name = "m_deleted_bv")
    private Long mDeletedBv;

    @Column(name = "m_created_at")
    private LocalDateTime mCreatedAt;

    @Column(name = "m_deleted-at")
    private LocalDateTime mDeletedAt;


    
    
    // PrePersist 로 현재 시간 설정 또는 삭제
    @PrePersist
    protected void onCreate() {
        mCreatedAt = LocalDateTime.now();
    }

    @PreRemove
    protected void onRemove() {
        mDeletedAt = LocalDateTime.now();
    }

}
