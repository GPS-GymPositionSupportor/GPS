package gps.base.Controller;

import gps.base.config.Elastic.GymDocument;
import gps.base.Service.GymSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/gym/search")
@RequiredArgsConstructor
public class GymSearchController {

    private final GymSearchService gymSearchService;

    @GetMapping
    public ResponseEntity<List<GymDocument>> searchGyms(@RequestParam String keyword) throws IOException {
        List<GymDocument> results = gymSearchService.searchGyms(keyword);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<GymDocument> addGym(@RequestBody GymDocument gymDocument) {
        GymDocument savedGym = gymSearchService.save(gymDocument);
        return ResponseEntity.ok(savedGym);
    }
}