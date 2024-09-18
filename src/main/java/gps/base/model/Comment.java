package gps.base.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long cId;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "gym_id", nullable = false)
    private Long gymId;

    @Column(name = "c_comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    // Constructor

    public Comment() {
    }

    public Comment(Long userId, Long gymId, String comment) {
        this.userId = userId;
        this.gymId = gymId;
        this.comment = comment;
    }

    // addedAt 입력 전, PrePersist 로 현재시간 설정
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
