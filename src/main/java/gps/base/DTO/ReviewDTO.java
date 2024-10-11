package gps.base.DTO;

import gps.base.model.Gym;
import gps.base.model.Member;
import lombok.*;

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


    public ReviewDTO(Long userId, Long gymId, String userName, String comment) {
        this.userId = userId;
        this.gymId = gymId;
        this.userName = userName;
        this.comment = comment;
    }
}
