package gps.base.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "favorite")
@IdClass(FavoriteId.class)
public class Favorite {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member userId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gymId;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;



    // Constructor

    public Favorite(Member userId, Gym gymId, boolean isFavorite) {
        this.userId = userId;
        this.gymId = gymId;
        this.isFavorite = isFavorite;
    }

    public Favorite() {

    }


    // 현재시간 자동 생성
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "user=" + (userId != null ? userId.getUserId() : null) +
                ", gym=" + (gymId != null ? gymId.getGymId() : null) +
                ", addedAt=" + addedAt +
                ", isFavorite=" + isFavorite +
                '}';
    }
}