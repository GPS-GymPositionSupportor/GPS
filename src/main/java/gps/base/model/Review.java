package gps.base.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "gym_id", nullable = false)
    private Long gymId;

    @Column(name = "r_comment", columnDefinition = "TEXT")
    private String comment;





    // 현재시간 자동 설정
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }

}
