package gps.base.search.ElasticEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "real_time_location")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RealTimeLocation {
    @Id
    private Long id;

    private Double longitude;
    private Double latitude;
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;
}