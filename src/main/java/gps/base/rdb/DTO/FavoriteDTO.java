package gps.base.rdb.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteDTO {
    private Long userId;
    private Long gymId;
    private Boolean isFavorite;
    private LocalDateTime addedAt;
}
