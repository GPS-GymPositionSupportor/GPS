package gps.base.search.ElasticEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "gym")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElasticGym {
    @Id
    @Column(name = "gym_id")
    private Long id;

    @Column(name = "g_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private ElasticGymCategory category;

    @Column(name = "address1")
    private String address1;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "g_longitude")
    private Double longitude;

    @Column(name = "g_latitude")
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
