package gps.base.controller;

import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Authority;
import gps.base.service.CommentService;
import gps.base.service.GymService;
import gps.base.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/stats")
@Slf4j
@RequiredArgsConstructor
public class StatisticsController {

    private final GymService gymService;
    private final ReviewService reviewService;
    private final CommentService commentService;
    private final JdbcTemplate jdbcTemplate;

    // 카테고리 별 시설 수 통계
    @GetMapping("/category")
    public ResponseEntity<List<Map<String, Object>>> getCategoryStats(HttpSession session) {
        Authority authority = (Authority) session.getAttribute("authority");
        if (authority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        List<Map<String, Object>> stats = jdbcTemplate.query(
                "SELECT category, COUNT(*) as count FROM gym GROUP BY category",
                (rs, rowNum) -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("category", rs.getString("category"));
                    stat.put("count", rs.getInt("count"));
                    return stat;
                }
        );
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/category-reviews")
    public ResponseEntity<List<Map<String, Object>>> getCategoryReviewStats(HttpSession session) {
        log.info("카테고리 리뷰 통계 API 호출됨");

        if ((Authority) session.getAttribute("authority") != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 카테고리별 통계를 미리 계산해서 캐시하는 것이 좋습니다
        String sql =
                "SELECT g.category, " +
                        "COUNT(DISTINCT r.r_id) as review_count " +
                        "FROM (SELECT DISTINCT category FROM gym WHERE category IN ('water', 'ball', 'physics', 'battle')) cats " +
                        "LEFT JOIN gym g ON cats.category = g.category " +
                        "LEFT JOIN review r ON g.gym_id = r.gym_id " +
                        "GROUP BY g.category " +
                        "ORDER BY FIELD(g.category, 'water', 'ball', 'physics', 'battle')";

        // 쿼리 타임아웃 설정
        jdbcTemplate.setQueryTimeout(30);  // 30초

        try {
            List<Map<String, Object>> stats = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Map<String, Object> stat = new HashMap<>();
                String category = rs.getString("category");
                int reviewCount = rs.getInt("review_count");

                String categoryKorean = switch (category) {
                    case "water" -> "수상 스포츠";
                    case "ball" -> "구기 스포츠";
                    case "physics" -> "피지컬";
                    case "battle" -> "격투기";
                    default -> category;
                };

                stat.put("category", categoryKorean);
                stat.put("reviewCount", reviewCount);

                return stat;
            });

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            log.error("카테고리 리뷰 통계 조회 중 오류 발생", e);
            throw new RuntimeException("카테고리 리뷰 통계 조회 실패: " + e.getMessage());
        }
    }


    @GetMapping("/user-types")
    public ResponseEntity<List<Map<String, Object>>> getUserTypeStats(HttpSession session) {
        Authority authority = (Authority) session.getAttribute("authority");
        if (authority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        List<Map<String, Object>> stats = jdbcTemplate.query(
                "SELECT provider_type as providerType, COUNT(*) as count " +
                        "FROM member " +
                        "GROUP BY provider_type",
                (rs, rowNum) -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("providerType", rs.getString("providerType"));
                    stat.put("count", rs.getInt("count"));
                    return stat;
                }
        );

        return ResponseEntity.ok(stats);
    }


}
