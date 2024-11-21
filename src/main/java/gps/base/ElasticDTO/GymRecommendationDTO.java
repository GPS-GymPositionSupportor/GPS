package gps.base.ElasticDTO;

import gps.base.ElasticSearchEntity.Gym;
import gps.base.ElasticSearchEntity.GymCategory;
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
    private GymCategory category;
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
    public static GymRecommendationDTO fromGym(Gym gym, Double similarityScore, Double distanceKm) {
        return new GymRecommendationDTO(
                gym.getId(),
                gym.getName(),
                gym.getCategory(),
                gym.getAddress1(),
                gym.getOpeningHours(),
                gym.getHomepage(),
                gym.getPhoneNumber(),
                gym.getRating(),
                gym.getLongitude(),
                gym.getLatitude(),
                similarityScore,
                distanceKm
        );
    }
}