package gps.base.ElasticSearchService;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import gps.base.config.Elastic.Document.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GymSearchService {
    private final ElasticsearchClient elasticsearchClient;

    /**
     * 위치 기반 근처 체육관 검색
     */
    public List<GymDocument> searchNearbyGyms(double lat, double lon, double distanceKm) throws IOException {
        Query geoQuery = Query.of(q -> q
                .bool(b -> b
                        .filter(f -> f
                                .geoDistance(g -> g
                                        .field("location")
                                        .distance(distanceKm + "km")
                                        .location(location -> location.coords(Arrays.asList(lon, lat)))))));

        SearchResponse<GymDocument> response = elasticsearchClient.search(s -> s
                        .index("gyms")
                        .query(geoQuery)
                        .sort(sort -> sort
                                .geoDistance(g -> g
                                        .field("location")
                                        .location(location -> location.coords(Arrays.asList(lon, lat)))
                                        .order(SortOrder.Asc))),
                GymDocument.class
        );

        return extractHits(response);
    }


    /**
     * 아이템 기반 협업 필터링을 사용하여 체육관 추천
     */
    public List<GymDocument> recommendSimilarGyms(String gymId, double lat, double lon) throws IOException {
        // 1. 타겟 체육관의 사용자 평점 데이터 조회
        GymDocument targetGym = getGymById(gymId);
        if (targetGym == null || targetGym.getUserInteractions() == null) {
            return Collections.emptyList();
        }

        // 2. 모든 체육관의 사용자 평점 데이터 조회
        List<GymDocument> allGyms = getAllGyms();
        if (allGyms.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 체육관 간 유사도 계산
        Map<String, Double> similarityScores = calculateGymSimilarities(targetGym, allGyms);

        // 4. 유사도가 높은 체육관들 필터링
        List<String> similarGymIds = similarityScores.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(gymId))
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (similarGymIds.isEmpty()) {
            return Collections.emptyList();
        }

        GeoLocation geoLocation = GeoLocation.of(b -> b.latlon(b2 -> b2.lat(lat).lon(lon)));
        List<FieldValue> fieldValues = similarGymIds.stream()
                .map(id -> FieldValue.of(id))
                .collect(Collectors.toList());

        // 5. 위치 기반 필터링 추가
        Query query = new Query.Builder()
                .bool(b -> b
                        .must(m -> m.terms(t -> t.field("_id").terms(TermsQueryField.of(f -> f.value(fieldValues)))))
                        .should(s -> s.geoDistance(g -> g
                                .field("location")
                                .distance("5km")
                                .location(geoLocation)))
                )
                .build();

        SearchResponse<GymDocument> response = elasticsearchClient.search(s -> s
                        .index("gyms")
                        .query(query)
                        .sort(sort -> sort.geoDistance(g -> g
                                .field("location")
                                .location(geoLocation)
                                .order(SortOrder.Asc))),
                GymDocument.class
        );


        return extractHits(response);
    }

    /**
     * 코사인 유사도를 사용하여 체육관 간 유사도 계산
     */
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
                similarity *= 1.2;
            }

            similarities.put(otherGym.getId(), similarity);
        }

        return similarities;
    }

    /**
     * 코사인 유사도 계산
     */
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

    /**
     * 체육관 ID로 체육관 정보 조회
     */
    private GymDocument getGymById(String gymId) throws IOException {
        Query idQuery = new Query.Builder()
                .term(t -> t
                        .field("_id")
                        .value(gymId))
                .build();

        SearchResponse<GymDocument> response = elasticsearchClient.search(s -> s
                        .index("gyms")
                        .query(idQuery),
                GymDocument.class
        );

        List<GymDocument> hits = extractHits(response);
        return hits.isEmpty() ? null : hits.get(0);
    }

    /**
     * 모든 체육관 정보 조회
     */
    private List<GymDocument> getAllGyms() throws IOException {
        SearchResponse<GymDocument> response = elasticsearchClient.search(s -> s
                        .index("gyms")
                        .query(q -> q
                                .matchAll(m -> m)),
                GymDocument.class
        );

        return extractHits(response);
    }

    /**
     * 검색 결과 추출 유틸리티 메서드
     */
    private List<GymDocument> extractHits(SearchResponse<GymDocument> response) {
        List<GymDocument> gyms = new ArrayList<>();
        for (Hit<GymDocument> hit : response.hits().hits()) {
            if (hit.source() != null) {
                gyms.add(hit.source());
            }
        }
        return gyms;
    }
}