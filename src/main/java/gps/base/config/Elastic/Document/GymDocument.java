package gps.base.config.Elastic.Document;

import gps.base.ElasticSearchEntity.GymCategory;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Document(indexName = "gyms")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class GymDocument {

    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private GymCategory category;

    @Field(type = FieldType.Text)
    private String address;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Float)
    private Double rating;

    @Field(type = FieldType.Text)
    private String openingHours;

    @Field(type = FieldType.Keyword)
    private String phoneNumber;

    @Field(type = FieldType.Nested)
    private List<UserInteraction> userInteractions;
}

