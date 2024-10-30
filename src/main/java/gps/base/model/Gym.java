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

    @Column(name = "homepage", nullable = true)
    private String homepage;

    @Column(name = "phone_number", nullable = true)
    private String phone;

    @Column(name = "g_longitude", nullable = false)
    private double gLongitude;

    @Column(name = "g_latitude", nullable = false)
    private double gLatitude;

    @Column(name = "information", nullable = false, columnDefinition = "TINYTEXT")
    private String information;

    @Column(name = "avg_rating", nullable = false)
    private double rating;

    @Column(name = "g_created_by", nullable = false)
    private String gCreatedBy;

    @Column(name = "g_deleted_by", nullable = false)
    private String gDeletedBy;

    @Column(name = "g_created_at", nullable = false)
    private LocalDateTime gCreatedAt;

    @Column(name = "g_deleted_at", nullable = false)
    private LocalDateTime gDeletedAt;


    // Constructors


    public Gym() {
    }

    public void setRating(Byte rating) {
        if(rating <= 1 || rating >= 5) {
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
