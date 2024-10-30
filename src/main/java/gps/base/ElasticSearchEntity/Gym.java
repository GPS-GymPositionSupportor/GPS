package gps.base.ElasticSearchEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// 체육관 정보 엔티티
@Entity
@Table(name = "gym")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Long gymId;

    @Column(name = "g_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GymCategory category;

    @Column(nullable = false)
    private String address1;

    @Column(nullable = false)
    private String address2;

    @Column(name = "opening_hours")
    private String openingHours;

    private String homepage;

    @Column(name = "phone_number")
    private String phoneNumber;

    private Double rating;

    @Column(name = "g_longitude", nullable = false)
    private Double longitude;

    @Column(name = "g_latitude", nullable = false)
    private Double latitude;

    @Column(name = "g_created_by")
    private String createdBy;

    @Column(name = "g_deleted_by")
    private String deletedBy;

    @Column(name = "g_created_at")
    private LocalDateTime createdAt;

    @Column(name = "g_deleted_at")
    private LocalDateTime deletedAt;

}

