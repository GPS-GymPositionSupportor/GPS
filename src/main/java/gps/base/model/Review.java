package gps.base.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    @JsonProperty("rId")
    private Long rId;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "gym_id", nullable = false)
    private Long gymId;

    @Column(name = "r_comment", columnDefinition = "TEXT")
    private String comment;

    // Member 정보를 가져오기
    @Transient
    private Member member;



    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    // 현재시간 자동 설정
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Review{" +
                "rId=" + rId +
                ", addedAt=" + addedAt +
                ", userId=" + userId +
                ", gymId=" + gymId +
                ", comment='" + comment + '\'' +
                ", comments=" + comments +
                '}';
    }
}
