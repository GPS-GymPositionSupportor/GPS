package gps.base.search.ElasticEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElasticAdmin {
    @Id
    @Column(name = "admin_id")
    private Long id;


    @JoinColumn(name = "user_id")
    private Long userId;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "a_id", nullable = false)
    private String aId;


    // 비밀번호 필드가 JSON 직렬화 과정에서 제외될 수 있게, JsonIgnore 어노테이션을 추가.
    @JsonIgnore
    @Column(name = "a_password", nullable = false)
    private String aPassword;
}