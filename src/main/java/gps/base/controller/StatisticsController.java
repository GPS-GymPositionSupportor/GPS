package gps.base.controller;

import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Authority;
import gps.base.service.CommentService;
import gps.base.service.GymService;
import gps.base.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/stats")
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
        Authority authority = (Authority) session.getAttribute("authority");
        if (authority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        List<Map<String, Object>> stats = jdbcTemplate.query(
                "SELECT g.category, COUNT(r.r_id) as review_count " +
                        "FROM gym g " +
                        "LEFT JOIN review r ON g.gym_id = r.gym_id " +
                        "GROUP BY g.category",
                (rs, rowNum) -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("category", rs.getString("category"));
                    stat.put("reviewCount", rs.getInt("review_count"));
                    return stat;
                }
        );

        return ResponseEntity.ok(stats);
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
