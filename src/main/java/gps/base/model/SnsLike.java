package gps.base.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "snslike")
@IdClass(SnsLikeId.class)
public class SnsLike {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @Column(name = "is_like", nullable = false)
    private Boolean isLike;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    // Constructor

    public SnsLike() {
    }


    //PrePersist 로 현재시간 자동 생성
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }

    // 디버깅시 필요
    @Override
    public String toString() {
        return "SnsLike{" +
                "user=" + user + (user != null ? user.getUserId() : null) +
                ", gym=" + gym + (gym != null ? gym.getGymId() : null) +
                ", addedAt=" + addedAt +
                ", isLike=" + isLike +
                ", likeCount=" + likeCount +
                '}';
    }
}

