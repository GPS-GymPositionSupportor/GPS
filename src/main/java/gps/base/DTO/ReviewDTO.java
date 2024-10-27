package gps.base.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long rId;   // 리뷰 ID 추가
    private LocalDateTime addedAt;  // 리뷰 작성 시간 추가
    private Long userId;
    private Long gymId;
    private String userName;    // Member의 이름
    private String comment;


    /*
    이미지 관련
     */
    private String reviewImage; // 리뷰 이미지
    private MultipartFile file; // 파일
    private String caption; // 이미지 이름

    // 다중 이미지를 위한 새 필드

    @JsonProperty("reviewImages")   // JSON 필드명 명시
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)    // 항상 직렬화
    private List<String> reviewImages = new ArrayList<>();  // 초기화

    public ReviewDTO(Long userId, Long gymId, String userName, String comment) {
        this.userId = userId;
        this.gymId = gymId;
        this.userName = userName;
        this.comment = comment;
    }


    // JPQL 쿼리용 생성자
    public ReviewDTO(Long rId, LocalDateTime addedAt, Long userId, Long gymId, String userName, String comment) {
        this.rId = rId;
        this.addedAt = addedAt;
        this.userId = userId;
        this.gymId = gymId;
        this.userName = userName;
        this.comment = comment;
    }

    // 명시적인 getter
    @JsonGetter("reviewImages")
    public List<String> getReviewImages() {
        if(reviewImages == null) {
            reviewImages = new ArrayList<>();
        }
        return reviewImages;
    }
}
