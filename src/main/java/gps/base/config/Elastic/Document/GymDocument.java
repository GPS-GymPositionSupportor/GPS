package gps.base.config.Elastic.Document;

import gps.base.ElasticSearchEntity.GymCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Document(indexName = "gyms")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class GymDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Double)
    private Double rating;

    @Field(type = FieldType.Nested)
    private List<UserInteraction> userInteractions;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Text)
    private String openingHours;

    @Field(type = FieldType.Keyword)
    private String phoneNumber;
}