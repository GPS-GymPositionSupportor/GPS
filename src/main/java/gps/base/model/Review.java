package gps.base.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.lang.reflect.Member;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long rId;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    // 다대 1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @Column(name = "gym_id", nullable = false)
    private String comment;



    // 현재시간 자동 설정
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }

}
