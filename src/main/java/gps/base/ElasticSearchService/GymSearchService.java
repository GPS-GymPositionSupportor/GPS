package gps.base.ElasticSearchService;

import gps.base.ElasticSearchEntity.Gym;
import gps.base.ElasticSearchEntity.Review;
import gps.base.config.Elastic.Document.GymDocument;
import gps.base.config.Elastic.GymSearchRepository;
import gps.base.repository.GymRepository;
import gps.base.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GymSearchService {
    private final GymSearchRepository gymSearchRepository;
    private final GymRepository gymRepository;
    private final ReviewRepository reviewRepository;

    private static final double NEARBY_DISTANCE_KM = 2.0;
    private static final int RECOMMEND_LIMIT = 5;


    public List<Gym> searchNearbyGyms(double lat, double lon, double distanceKm) {
        List<GymDocument> nearbyGymDocs = gymSearchRepository.searchByLocation(distanceKm, lat, lon);

        // 거리 계산 및 정렬을 위한 Map 생성
        Map<Long, Double> distanceMap = new HashMap<>();

        // 각 체육관의 거리 계산
        for (GymDocument doc : nearbyGymDocs) {
            double distance = calculateDistance(lat, lon, doc.getLatitude(), doc.getLongitude());
            distanceMap.put(Long.parseLong(doc.getId()), distance);
        }

        // 체육관 정보 조회 및 거리순 정렬
        return nearbyGymDocs.stream()
                .map(doc -> gymRepository.findById(Long.parseLong(doc.getId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparingDouble(gym ->
                        distanceMap.getOrDefault(gym.getId(), Double.MAX_VALUE)))
                .collect(Collectors.toList());
    }

    // 두 지점 간의 거리 계산 (Haversine formula)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public List<Gym> recommendNearbyGyms(double userLat, double userLon) {
        // 1. 현재 위치 기준 2km 이내의 체육관들을 검색
        List<GymDocument> nearbyGymDocs = gymSearchRepository.findByLocationNear(NEARBY_DISTANCE_KM, userLat, userLon);

        if (nearbyGymDocs.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 근처 체육관들의 상세 정보 조회
        List<Gym> nearbyGyms = nearbyGymDocs.stream()
                .map(doc -> gymRepository.findById(Long.parseLong(doc.getId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // 3. 각 체육관 쌍 간의 유사도 계산
        Map<Long, Map<Long, Double>> similarityMatrix = new HashMap<>();
        for (Gym gym1 : nearbyGyms) {
            similarityMatrix.put(gym1.getId(), new HashMap<>());
            for (Gym gym2 : nearbyGyms) {
                if (gym1.getId().equals(gym2.getId()) ||
                        !gym1.getCategory().equals(gym2.getCategory())) {
                    continue;
                }

                double similarity = calculateGymSimilarity(gym1, gym2);
                similarityMatrix.get(gym1.getId()).put(gym2.getId(), similarity);
            }
        }

        // 4. 각 체육관의 최종 점수 계산 (유사도 + 거리 + 평점)
        List<GymScore> gymScores = nearbyGyms.stream()
                .map(gym -> {
                    double similarityScore = calculateAverageSimilarity(gym.getId(), similarityMatrix);
                    double distance = calculateDistance(userLat, userLon, gym.getLatitude(), gym.getLongitude());
                    double rating = gym.getRating() != null ? gym.getRating() : 0.0;

                    return new GymScore(gym, similarityScore, distance, rating);
                })
                .collect(Collectors.toList());

        // 5. 점수 기반으로 정렬하여 상위 5개 추천
        return gymScores.stream()
                .sorted(Comparator.comparing(GymScore::getCalculatedScore).reversed())
                .limit(RECOMMEND_LIMIT)
                .map(GymScore::getGym)
                .collect(Collectors.toList());
    }

    /**
     * 두 체육관 간의 유사도 계산
     */
    private double calculateGymSimilarity(Gym gym1, Gym gym2) {
        List<Review> reviews1 = reviewRepository.findByGymId(gym1.getId());
        List<Review> reviews2 = reviewRepository.findByGymId(gym2.getId());

        Map<Long, Double> ratings1 = reviews1.stream()
                .collect(Collectors.toMap(
                        Review::getUserId,
                        Review::getRating,
                        (r1, r2) -> r1
                ));

        Map<Long, Double> ratings2 = reviews2.stream()
                .collect(Collectors.toMap(
                        Review::getUserId,
                        Review::getRating,
                        (r1, r2) -> r1
                ));

        // 공통 사용자 찾기
        Set<Long> commonUsers = new HashSet<>(ratings1.keySet());
        commonUsers.retainAll(ratings2.keySet());

        if (commonUsers.isEmpty()) {
            return 0.0;
        }

        // 코사인 유사도 계산
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Long userId : commonUsers) {
            double rating1 = ratings1.get(userId);
            double rating2 = ratings2.get(userId);

            dotProduct += rating1 * rating2;
            norm1 += rating1 * rating1;
            norm2 += rating2 * rating2;
        }

        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);

        return norm1 == 0.0 || norm2 == 0.0 ? 0.0 : dotProduct / (norm1 * norm2);
    }

    /**
     * 한 체육관과 다른 체육관들 간의 평균 유사도 계산
     */
    private double calculateAverageSimilarity(Long gymId, Map<Long, Map<Long, Double>> similarityMatrix) {
        Map<Long, Double> similarities = similarityMatrix.get(gymId);
        if (similarities.isEmpty()) {
            return 0.0;
        }
        return similarities.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }


}