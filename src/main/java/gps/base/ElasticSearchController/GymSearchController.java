package gps.base.ElasticSearchController;

import gps.base.ElasticSearchService.GymSearchService;
import gps.base.config.Elastic.Document.GymDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
@Slf4j
public class GymSearchController {
    private final GymSearchService gymSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<GymDocument>> searchNearbyGyms(
            @RequestParam(name = "lat") double lat,
            @RequestParam(name = "lon") double lon,
            @RequestParam(name = "distance", defaultValue = "5.0") double distance
    ) {
        try {
            List<GymDocument> results = gymSearchService.searchNearbyGyms(lat, lon, distance);
            log.info("Found {} gyms near lat: {}, lon: {}", results.size(), lat, lon);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error searching nearby gyms: ", e);
            throw new RuntimeException("Failed to search nearby gyms", e);
        }
    }


    @GetMapping("/recommend/similar")
    public ResponseEntity<List<GymDocument>> recommendSimilarGyms(
            @RequestParam(name = "gymId") String gymId,
            @RequestParam(name = "lat") double lat,
            @RequestParam(name = "lon") double lon
    ) throws IOException {
        return ResponseEntity.ok(
                gymSearchService.recommendSimilarGyms(gymId, lat, lon)
        );
    }
}
