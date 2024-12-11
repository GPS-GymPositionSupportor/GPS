package gps.base.search.ElasticEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {
    @Id
    @Column(name = "admin_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}