package gps.base.gymdata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "gym")
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Long gymId;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "g_name", nullable = false)
    private String gName;

    @Column(name = "g_longitude", nullable = false)
    private double gLongitude;

    @Column(name = "g_latitude", nullable = false)
    private double gLatitude;

    @Column(name = "information", columnDefinition = "TINYTEXT", nullable = true) // nullable 설정
    private String information;

    @Column(name = "gym_image", nullable = true) // nullable 설정
    private String gymImage;

    @Column(name = "rating", nullable = true) // nullable 설정
    private Byte rating;

    @Column(name = "g_created_by", nullable = true) // nullable 설정
    private String gCreatedBy;

    @Column(name = "g_deleted_by", nullable = true) // nullable 설정
    private String gDeletedBy;

    @Column(name = "g_created_at", nullable = true) // nullable 설정
    private LocalDateTime gCreatedAt;

    @Column(name = "g_deleted_at", nullable = true) // nullable 설정
    private LocalDateTime gDeletedAt;

    // 생성일 자동 설정
    @PrePersist
    protected void onCreate() {
        gCreatedAt = LocalDateTime.now();
    }

    public void setRating(Byte rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("점수는 1점과 5점 사이로만 입력 되어야 합니다.");
        }
        this.rating = rating;
    }

    // 기본 생성자
    public Gym() {
    }
}
