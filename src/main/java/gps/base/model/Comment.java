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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Column(name = "c_comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    // Constructor

    public Comment() {
    }

    public Comment(Member user, Gym gym, String comment) {
        this.user = user;
        this.gym = gym;
        this.comment = comment;
    }

    // addedAt 입력 전, PrePersist 로 현재시간 설정
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
