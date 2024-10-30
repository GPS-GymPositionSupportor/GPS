package gps.base.ElasticSearchEntity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;



@Embeddable
@Data
public class FavoriteId implements Serializable {
    private Long userId;
    private Long gymId;
}
