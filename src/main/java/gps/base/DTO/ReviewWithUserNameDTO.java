package gps.base.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewWithUserNameDTO {
    @JsonProperty("rId")
    private Long rId;
    private LocalDateTime addedAt;
    private Long userId;
    private String userName;
    private Long gymId;
    private String comment;

    public ReviewWithUserNameDTO() {
    }

    public ReviewWithUserNameDTO(Long rId, LocalDateTime addedAt, Long userId, String userName, Long gymId, String comment) {
        this.rId = rId;
        this.addedAt = addedAt;
        this.userId = userId;
        this.userName = userName;
        this.gymId = gymId;
        this.comment = comment;
    }
}
