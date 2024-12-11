package gps.base.search.ElasticDTO;

import gps.base.search.ElasticEntity.ElasticGym;
import gps.base.search.ElasticEntity.ElasticGymCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GymRecommendationDTO {
    private Long id;
    private String name;
    private ElasticGymCategory category;
    private String address1;
    private String openingHours;
    private String homepage;
    private String phoneNumber;
    private Double rating;
    private Double longitude;
    private Double latitude;
    private Double similarityScore;  // 유사도 점수
    private Double distanceKm;       // 거리 정보

    // Gym 엔티티로부터 DTO 생성
    public static GymRecommendationDTO fromGym(ElasticGym elasticGym, Double similarityScore, Double distanceKm) {
        return new GymRecommendationDTO(
                elasticGym.getId(),
                elasticGym.getName(),
                elasticGym.getCategory(),
                elasticGym.getAddress1(),
                elasticGym.getOpeningHours(),
                elasticGym.getHomepage(),
                elasticGym.getPhoneNumber(),
                elasticGym.getRating(),
                elasticGym.getLongitude(),
                elasticGym.getLatitude(),
                similarityScore,
                distanceKm
        );
    }
}