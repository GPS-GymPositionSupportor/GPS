package gps.base.config.Elastic.Document;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserInteraction {
    private String userId;
    private Double rating;
}