package gps.base.ElasticSearchEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sns_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsLike {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @Column(name = "is_like")
    private boolean isLike;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "added_at")
    private LocalDateTime addedAt;
}