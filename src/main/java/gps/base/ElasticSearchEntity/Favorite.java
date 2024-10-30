package gps.base.ElasticSearchEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 회원 즐겨찾기 엔티티
@Entity
@Table(name = "favorite")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @EmbeddedId
    private FavoriteId id;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @Column(name = "is_favorite", nullable = false,columnDefinition = "TINYINT(1)")
    private Boolean isFavorite;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @MapsId("gymId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;
}

