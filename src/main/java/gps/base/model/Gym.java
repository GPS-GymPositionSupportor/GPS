package gps.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "gym")
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Long gymId;

    @Column(name = "address1", nullable = false, unique = true)
    private String address;

    @Column(name = "g_name", nullable = false)
    private String gName;

    @Column(name = "opening_hours", nullable = true)
    private String openHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private GymCategory category;

    @Column(name = "homepage", nullable = true, length = 1000)
    private String homepage;

    @Column(name = "phone_number", nullable = true)
    private String phone;

    @Column(name = "g_longitude")
    private double gLongitude;

    @Column(name = "g_latitude")
    private double gLatitude;

    @Column(name = "rating", nullable = true)
    private Double rating = 0.0;

    @Column(name = "g_created_by")
    private String gCreatedBy;

    @Column(name = "g_deleted_by")
    private String gDeletedBy;

    @Column(name = "g_created_at")
    private LocalDateTime gCreatedAt;

    @Column(name = "g_deleted_at")
    private LocalDateTime gDeletedAt;


    // Constructors


    public Gym() {
    }

    public void setRating(Double rating) {
        if(rating == null) {
            this.rating = 0.0;  // null일 경우 기본값 0.0
            return;
        }
        if(rating < 1.0 || rating > 5.0) {  // <= 대신 < 사용
            throw new IllegalArgumentException("점수는 1점과 5점 사이로만 입력 되어야 합니다.");
        }
        this.rating = rating;
    }


    // gCreateAt 을 입력하기 전에 PrePersist로 세팅하기
    @PrePersist
    protected void onCreate() {
        gCreatedAt = LocalDateTime.now();
    }
}
