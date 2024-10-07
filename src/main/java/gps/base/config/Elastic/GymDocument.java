package gps.base.config.Elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

//엘라스틱 문서 클래스, 정보의 종류
@Document(indexName = "gym")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private String g_name;

    @Field(type = FieldType.Text)
    private String information;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Double)
    private Double g_latitude;

    @Field(type = FieldType.Double)
    private Double g_longitude;
}