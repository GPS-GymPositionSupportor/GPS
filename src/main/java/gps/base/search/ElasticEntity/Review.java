package gps.base.search.ElasticEntity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "gym_id")
    private Long gymId;

    @Column(name = "r_comment")
    private String rComment;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Column(nullable = false)
    private Double rating;
}