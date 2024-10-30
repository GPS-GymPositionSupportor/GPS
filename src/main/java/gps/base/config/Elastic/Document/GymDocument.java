package gps.base.config.Elastic.Document;

import gps.base.ElasticSearchEntity.GymCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Document(indexName = "gyms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GymDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private GymCategory category;

    @Field(type = FieldType.Text)
    private String address;

    private GeoPoint location;

    @Field(type = FieldType.Keyword)
    private List<String> facilities;

    @Field(type = FieldType.Double)
    private double rating;

    @Field(type = FieldType.Text)
    private String openingHours;

    @Field(type = FieldType.Keyword)
    private String phoneNumber;

    @Field(type = FieldType.Nested)
    private List<UserInteraction> userInteractions;
}
