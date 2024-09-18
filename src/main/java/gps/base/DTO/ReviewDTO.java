package gps.base.DTO;

import gps.base.model.Gym;
import gps.base.model.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    private Long userId;
    private Long gymId;
    private String comment;
}
