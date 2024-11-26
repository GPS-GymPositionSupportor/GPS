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

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "korean", searchAnalyzer = "korean"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String name;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Text, analyzer = "korean", searchAnalyzer = "korean")
    private String address1;

    @Field(type = FieldType.Double)
    private Double latitude;

    @Field(type = FieldType.Double)
    private Double longitude;

    @Field(type = FieldType.Double)
    private Double rating;

    @GeoPointField
    private GeoPoint location;
}