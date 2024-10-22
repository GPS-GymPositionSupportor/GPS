package gps.base.DTO;

import gps.base.model.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CommentDTO {
    private Long userId;
    private Long gymId;
    private String comment;
}
