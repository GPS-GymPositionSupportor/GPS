package gps.base.ElasticDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymSearchDTO {
    private String name;
    private String correctedName;
    private boolean isCorrected;
}