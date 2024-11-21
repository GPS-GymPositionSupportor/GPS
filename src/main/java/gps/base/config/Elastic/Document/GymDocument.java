package gps.base.config.Elastic.Document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "gyms")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class GymDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Double)
    private Double latitude;

    @Field(type = FieldType.Double)
    private Double longitude;

    @GeoPointField
    private GeoPoint location;
}