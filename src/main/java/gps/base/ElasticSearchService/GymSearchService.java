package gps.base.ElasticSearchService;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import gps.base.config.Elastic.Document.*;
import gps.base.ElasticSearchEntity.Gym;
import gps.base.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GymSearchService {
    private final ElasticsearchClient elasticsearchClient;
    private final GymRepository gymRepository;
    private static final String INDEX_NAME = "gyms";

    public List<Gym> searchNearbyGyms(double lat, double lon, double distanceKm) {
        try {
            GeoLocation geoLocation = GeoLocation.of(builder -> builder
                    .latlon(latlon -> latlon
                            .lat(lat)
                            .lon(lon)));

            Query geoQuery = Query.of(q -> q
                    .bool(b -> b
                            .must(m -> m
                                    .geoDistance(g -> g
                                            .field("location")
                                            .location(geoLocation)
                                            .distance(distanceKm + "km")))));

            SearchResponse<GymDocument> response = elasticsearchClient.search(s -> s
                            .index(INDEX_NAME)
                            .query(geoQuery)
                            .size(100),
                    GymDocument.class);

            List<Long> gymIds = response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .map(doc -> Long.parseLong(doc.getId()))
                    .collect(Collectors.toList());

            return gymRepository.findAllById(gymIds);

        } catch (Exception e) {
            log.error("Error in searchNearbyGyms: ", e);
            return Collections.emptyList();
        }
    }

    public List<Gym> recommendSimilarGyms(String gymId, double lat, double lon) throws IOException {
        // ElasticSearch로 유사도 계산
        GymDocument targetGym = getGymById(gymId);
        if (targetGym == null || targetGym.getUserInteractions() == null) {
            return Collections.emptyList();
        }

        List<GymDocument> allGyms = getAllGyms();
        if (allGyms.isEmpty()) {
            return Collections.emptyList();
        }

        // 코사인 유사도 계산
        Map<String, Double> similarityScores = calculateGymSimilarities(targetGym, allGyms);

        List<String> similarGymIds = similarityScores.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(gymId))
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (similarGymIds.isEmpty()) {
            return Collections.emptyList();
        }

        // JPA로 실제 데이터 조회
        List<Long> dbGymIds = similarGymIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return gymRepository.findAllById(dbGymIds);
    }

    private Map<String, Double> calculateGymSimilarities(GymDocument targetGym, List<GymDocument> allGyms) {
        Map<String, Double> similarities = new HashMap<>();
        Map<String, Map<String, Double>> userRatings = new HashMap<>();

        // 모든 체육관의 사용자 평점을 맵으로 변환
        for (GymDocument gym : allGyms) {
            Map<String, Double> ratings = new HashMap<>();
            if (gym.getUserInteractions() != null) {
                for (UserInteraction interaction : gym.getUserInteractions()) {
                    ratings.put(interaction.getUserId(), interaction.getRating());
                }
            }
            userRatings.put(gym.getId(), ratings);
        }

        // 타겟 체육관의 사용자 평점
        Map<String, Double> targetRatings = userRatings.get(targetGym.getId());

        // 각 체육관과의 유사도 계산
        for (GymDocument otherGym : allGyms) {
            if (otherGym.getId().equals(targetGym.getId())) continue;

            Map<String, Double> otherRatings = userRatings.get(otherGym.getId());
            double similarity = calculateCosineSimilarity(targetRatings, otherRatings);

            if (targetGym.getCategory() == otherGym.getCategory()) {
                similarity *= 1.2; // 같은 카테고리일 경우 가중치 부여
            }

            similarities.put(otherGym.getId(), similarity);
        }

        return similarities;
    }

    private double calculateCosineSimilarity(Map<String, Double> ratings1, Map<String, Double> ratings2) {
        Set<String> commonUsers = new HashSet<>(ratings1.keySet());
        commonUsers.retainAll(ratings2.keySet());

        if (commonUsers.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String userId : commonUsers) {
            double r1 = ratings1.get(userId);
            double r2 = ratings2.get(userId);

            dotProduct += r1 * r2;
            norm1 += r1 * r1;
            norm2 += r2 * r2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) return 0.0;

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private GymDocument getGymById(String gymId) throws IOException {
        Query idQuery = new Query.Builder()
                .term(t -> t
                        .field("_id")
                        .value(gymId))
                .build();

        SearchResponse<GymDocument> response = elasticsearchClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(idQuery),
                GymDocument.class
        );

        List<GymDocument> hits = extractHits(response);
        return hits.isEmpty() ? null : hits.get(0);
    }

    private List<GymDocument> getAllGyms() throws IOException {
        SearchResponse<GymDocument> response = elasticsearchClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q
                                .matchAll(m -> m)),
                GymDocument.class
        );

        return extractHits(response);
    }

    private List<GymDocument> extractHits(SearchResponse<GymDocument> response) {
        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}