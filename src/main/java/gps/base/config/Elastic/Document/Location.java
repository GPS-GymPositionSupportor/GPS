package gps.base.config.Elastic.Document;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Location {
    private Double lat;
    private Double lon;
}
