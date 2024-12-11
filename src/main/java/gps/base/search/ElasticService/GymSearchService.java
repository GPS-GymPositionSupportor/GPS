package gps.base.search.ElasticService;


import gps.base.search.ElasticConfig.Document.GymDocument;
import gps.base.search.ElasticEntity.ElasticGym;
import gps.base.search.ElasticEntity.ElasticGymCategory;
import gps.base.search.ElasticEntity.ElasticReview;
import gps.base.search.ElasticRepository.ElasticGymRepository;
import gps.base.search.ElasticRepository.GymSearchRepository;
import gps.base.search.ElasticRepository.ElasticReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GymSearchService {
    private final GymSearchRepository gymSearchRepository;
    private final ElasticGymRepository elasticGymRepository;
    private final ElasticReviewRepository elasticReviewRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    private static final double NEARBY_DISTANCE_KM = 2.0;
    private static final int RECOMMEND_LIMIT = 5;


    public List<ElasticGym> searchNearbyGyms(double lat, double lon, double distanceKm) {
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
                .map(doc -> elasticGymRepository.findById(Long.parseLong(doc.getId())))
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

    public List<ElasticGym> recommendNearbyGyms(double userLat, double userLon) {
        // 1. 현재 위치 기준 2km 이내의 체육관들을 검색
        List<GymDocument> nearbyGymDocs = gymSearchRepository.findByLocationNear(NEARBY_DISTANCE_KM, userLat, userLon);

        if (nearbyGymDocs.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 근처 체육관들의 상세 정보 조회
        List<ElasticGym> nearbyElasticGyms = nearbyGymDocs.stream()
                .map(doc -> elasticGymRepository.findById(Long.parseLong(doc.getId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // 3. 각 체육관 쌍 간의 유사도 계산
        Map<Long, Map<Long, Double>> similarityMatrix = new HashMap<>();
        for (ElasticGym elasticGym1 : nearbyElasticGyms) {
            similarityMatrix.put(elasticGym1.getId(), new HashMap<>());
            for (ElasticGym elasticGym2 : nearbyElasticGyms) {
                if (elasticGym1.getId().equals(elasticGym2.getId()) ||
                        !elasticGym1.getCategory().equals(elasticGym2.getCategory())) {
                    continue;
                }

                double similarity = calculateGymSimilarity(elasticGym1, elasticGym2);
                similarityMatrix.get(elasticGym1.getId()).put(elasticGym2.getId(), similarity);
            }
        }

        // 4. 각 체육관의 최종 점수 계산 (유사도 + 거리 + 평점)
        List<GymScore> gymScores = nearbyElasticGyms.stream()
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
                .map(GymScore::getElasticGym)
                .collect(Collectors.toList());
    }

    /**
     * 두 체육관 간의 유사도 계산
     */
    private double calculateGymSimilarity(ElasticGym elasticGym1, ElasticGym elasticGym2) {

        double[] vector1 = {
                elasticGym1.getRating(),
                elasticGym1.getLatitude(),
                elasticGym1.getLongitude()
                // 필요한 다른 특성들 추가 가능
        };

        double[] vector2 = {
                elasticGym2.getRating(),
                elasticGym2.getLatitude(),
                elasticGym2.getLongitude()
                // 필요한 다른 특성들 추가 가능
        };

        // 코사인 유사도 계산
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
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



    public List<ElasticGym> searchByKeywordAndSort(String keyword, double lat, double lon) {
        log.info("Searching for gyms with keyword: {} near location: ({}, {})", keyword, lat, lon);

        // 키워드로 검색
        List<GymDocument> searchResults = gymSearchRepository.searchByKeyword(keyword);
        log.info("Found {} documents in Elasticsearch", searchResults.size());

        if (searchResults.isEmpty()) {
            return Collections.emptyList();
        }

        // ID로 체육관 정보 조회
        List<Long> gymIds = searchResults.stream()
                .map(doc -> Long.parseLong(doc.getId()))
                .collect(Collectors.toList());

        List<ElasticGym> elasticGyms = elasticGymRepository.findAllById(gymIds);

        // 거리 계산 및 정렬을 위한 데이터 구조
        List<GymWithDistance> gymsWithDistance = elasticGyms.stream()
                .map(gym -> new GymWithDistance(gym,
                        calculateDistance(lat, lon, gym.getLatitude(), gym.getLongitude())))
                .collect(Collectors.toList());

        // 거리순 정렬
        gymsWithDistance.sort(Comparator.comparingDouble(GymWithDistance::getDistance));

        // 결과 반환
        return gymsWithDistance.stream()
                .map(GymWithDistance::getElasticGym)
                .collect(Collectors.toList());
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    private static class GymWithDistance {
        private ElasticGym elasticGym;
        private double distance;
    }

    /**
     * 키워드와 카테고리로 체육관 검색
     */
    public List<ElasticGym> searchByKeywordAndCategory(String keyword, ElasticGymCategory category) {
        List<GymDocument> searchResults = gymSearchRepository.searchByKeywordAndCategory(keyword, category.name());
        return convertToGymList(searchResults);
    }

    /**
     * ElasticSearch 검색 결과를 JPA 엔티티로 변환
     */
    private List<ElasticGym> convertToGymList(List<GymDocument> documents) {
        return documents.stream()
                .map(doc -> elasticGymRepository.findById(Long.parseLong(doc.getId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}