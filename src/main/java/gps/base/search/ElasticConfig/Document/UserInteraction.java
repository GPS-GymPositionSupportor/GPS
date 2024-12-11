package gps.base.search.ElasticConfig.Document;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter @Setter
@Document(indexName = "user_interactions")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserInteraction {
    private String userId;
    private String gymId;
    private InteractionType type;
    private Double score;  // 각 상호작용의 가중치 점수
    private Long timestamp;

    // 각 상호작용 타입별 가중치 정의
    public static double getWeightForInteractionType(InteractionType type) {
        return switch (type) {
            case VISIT -> 0.3;     // 방문은 기본 가중치
            case REVIEW -> 0.4;    // 리뷰 작성은 더 높은 가중치
            case BOOKMARK -> 0.3;  // 즐겨찾기는 방문과 동일 가중치
        };
    }
}