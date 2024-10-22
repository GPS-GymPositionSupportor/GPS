package gps.base.DTO;

import gps.base.model.Gym;
import gps.base.model.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
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


    public ReviewDTO(Long userId, Long gymId, String userName, String comment) {
        this.userId = userId;
        this.gymId = gymId;
        this.userName = userName;
        this.comment = comment;
    }
}
