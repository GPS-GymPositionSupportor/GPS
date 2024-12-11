package gps.base.search.ElasticEntity;

import gps.base.rdb.model.FavoriteId;
import gps.base.rdb.model.Gym;
import gps.base.rdb.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "favorite")
@Data
@IdClass(FavoriteId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElasticFavorite {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;    // id 값이 x

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;

}


