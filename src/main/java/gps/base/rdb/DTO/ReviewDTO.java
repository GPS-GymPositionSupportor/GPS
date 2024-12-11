package gps.base.rdb.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gps.base.rdb.model.Review;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long rId;
    private LocalDateTime addedAt;
    private Long userId;
    private Long gymId;
    private String userName;
    private String comment;
    private String formattedDate;  // 날짜 포맷팅용



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

    public static ReviewDTO fromEntity(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setRId(review.getRId());
        dto.setAddedAt(review.getAddedAt());
        dto.setUserId(review.getUserId());
        dto.setGymId(review.getGymId());  // 연관된 Gym에서 ID 가져오기
        dto.setUserName(review.getMember().getName());
        dto.setComment(review.getComment());

        return dto;
    }

    // 명시적인 getter
    @JsonGetter("reviewImages")
    public List<String> getReviewImages() {
        if(reviewImages == null) {
            reviewImages = new ArrayList<>();
        }
        return reviewImages;
    }

    public void setFormattedDate(String format) {

    }
}