package gps.base.search.ElasticEntity;

import gps.base.rdb.model.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
public class ElasticComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_id")
    private Review review;

    @Column(name = "c_comment", nullable = false, columnDefinition = "TEXT")
    private String comment;
}