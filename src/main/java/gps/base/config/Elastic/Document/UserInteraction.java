package gps.base.config.Elastic.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInteraction {
    private String userId;
    private double rating;
    private String visitDate;
    private InteractionType type;
}
