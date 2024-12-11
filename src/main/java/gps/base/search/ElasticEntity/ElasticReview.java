package gps.base.search.ElasticEntity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "review")
public class ElasticReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long rId;

    @JoinColumn(name = "user_id")
    private Long userId;

    @JoinColumn(name = "gym_id")
    private Long gymId;

    @Column(name = "r_comment")
    private String rComment;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

}