package gps.base.DTO;

import gps.base.model.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String comment;
    private String userName;
    private LocalDateTime createdAt;
    private Long reviewId;
}
