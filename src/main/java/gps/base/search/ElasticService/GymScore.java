package gps.base.search.ElasticService;


import gps.base.search.ElasticEntity.Gym;
import lombok.Getter;

@Getter
public class GymScore {
    private static final double NEARBY_DISTANCE_KM = 2.0;

    private final Gym gym;
    private final Double similarityScore;
    private final Double distance;
    private final Double rating;

    public GymScore(Gym gym, Double similarityScore, Double distance, Double rating) {
        this.gym = gym;
        this.similarityScore = similarityScore;
        this.distance = distance;
        this.rating = rating;
    }

    // 종합 점수 계산 (유사도 * 0.4 + 평점 * 0.4 + 거리 점수 * 0.2)
    public double getCalculatedScore() {
        double normalizedSimilarity = similarityScore * 0.4;  // 유사도 가중치
        double ratingScore = (rating != null ? rating / 5.0 : 0.0) * 0.4;  // 평점 가중치 (5점 만점 기준)
        double distanceScore = (1 - Math.min(distance / NEARBY_DISTANCE_KM, 1.0)) * 0.2;  // 거리 가중치

        return normalizedSimilarity + ratingScore + distanceScore;
    }
}