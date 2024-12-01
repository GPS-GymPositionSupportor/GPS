package gps.base.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FavoriteDto {
    private Long userId;
    private Long gymId;
    private Boolean isFavorite;
    private LocalDateTime addedAt;
}
