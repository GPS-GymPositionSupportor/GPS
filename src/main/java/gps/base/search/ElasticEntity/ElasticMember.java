package gps.base.search.ElasticEntity;

import gps.base.rdb.model.Authority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElasticMember {
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
}
