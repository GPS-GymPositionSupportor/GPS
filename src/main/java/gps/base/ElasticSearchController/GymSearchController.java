package gps.base.ElasticSearchController;

import gps.base.ElasticDTO.LocationDTO;
import gps.base.ElasticSearchEntity.GymCategory;
import gps.base.ElasticSearchService.GymSearchService;
import gps.base.ElasticSearchEntity.Gym;
import gps.base.ElasticSearchService.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
@Slf4j
public class GymSearchController {
    private final GymSearchService gymSearchService;
    private final LocationService locationService;


    @GetMapping("/search/current-location")
    public ResponseEntity<List<Gym>> searchNearbyGymsFromCurrentLocation(
            @RequestParam(name = "distance", defaultValue = "5.0") double distance
    ) {
        try {
            LocationDTO currentLocation = locationService.getCurrentLocation();
            List<Gym> results = gymSearchService.searchNearbyGyms(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude(),
                    distance
            );
            log.info("Found {} gyms near current location", results.size());
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error searching nearby gyms from current location: ", e);
            throw new RuntimeException("Failed to search nearby gyms", e);
        }
    }

    @GetMapping("/recommend/current-location")
    public ResponseEntity<List<Gym>> getRecommendedGymsFromCurrentLocation() {
        LocationDTO currentLocation = locationService.getCurrentLocation();
        List<Gym> recommendations = gymSearchService.recommendNearbyGyms(
                currentLocation.getLatitude(),
                currentLocation.getLongitude()
        );
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/search/keyword/current-location")
    public ResponseEntity<List<Gym>> searchByKeywordFromCurrentLocation(
            @RequestParam String keyword) {
        try {
            LocationDTO currentLocation = locationService.getCurrentLocation();
            log.info("Searching gyms with keyword: {} at current location", keyword);
            List<Gym> results = gymSearchService.searchByKeywordAndSort(
                    keyword,
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
            );
            log.info("Found {} results for keyword: {}", results.size(), keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error searching with keyword from current location: {}", keyword, e);
            throw e;
        }
    }

    @GetMapping("/search/nearby")
    public ResponseEntity<List<Gym>> searchNearbyGyms(
            @RequestParam(name = "lat") double lat,
            @RequestParam(name = "lon") double lon,
            @RequestParam(name = "distance", defaultValue = "5.0") double distance
    ) {
        try {
            List<Gym> results = gymSearchService.searchNearbyGyms(lat, lon, distance);
            log.info("Found {} gyms near lat: {}, lon: {}", results.size(), lat, lon);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error searching nearby gyms: ", e);
            throw new RuntimeException("Failed to search nearby gyms", e);
        }
    }

    @GetMapping("/recommend/nearby")
    public ResponseEntity<List<Gym>> getRecommendedNearbyGyms(
            @RequestParam double lat,
            @RequestParam double lon) {

        List<Gym> recommendations = gymSearchService.recommendNearbyGyms(lat, lon);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<List<Gym>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(required = true) Double lat,
            @RequestParam(required = true) Double lon) {
        try {
            log.info("Searching gyms with keyword: {} at location ({}, {})", keyword, lat, lon);
            List<Gym> results = gymSearchService.searchByKeywordAndSort(keyword, lat, lon);
            log.info("Found {} results for keyword: {}", results.size(), keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error searching with keyword: {}", keyword, e);
            throw e;
        }
    }

    @GetMapping("/search/keyword-category")
    public ResponseEntity<List<Gym>> searchByKeywordAndCategory(
            @RequestParam String keyword,
            @RequestParam GymCategory category) {
        log.info("Searching gyms with keyword: {} and category: {}", keyword, category);
        List<Gym> results = gymSearchService.searchByKeywordAndCategory(keyword, category);
        return ResponseEntity.ok(results);
    }


}