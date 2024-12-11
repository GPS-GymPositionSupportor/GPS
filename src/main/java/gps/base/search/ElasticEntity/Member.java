package gps.base.search.ElasticEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "m_id")
    private String memberId;

    @Column(name = "m_password")
    private String password;

    private String name;
    private String nickname;
    private String email;
    private LocalDate birth;
    private String gender;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "m_created_by")
    private String createdBy;

    @Column(name = "m_deleted_by")
    private String deletedBy;

    @Column(name = "m_created_at")
    private LocalDateTime createdAt;

    @Column(name = "m_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}

