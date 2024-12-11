package gps.base.rdb.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String comment;
    private String userName;
    private LocalDateTime createdAt;
    private Long reviewId;
}
