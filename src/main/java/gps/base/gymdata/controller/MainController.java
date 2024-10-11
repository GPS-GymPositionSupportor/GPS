package gps.base.gymdata.controller;

import gps.base.gymdata.DTO.GymDTO;
import gps.base.gymdata.KakaoGymCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private KakaoGymCrawler kakaoGymCrawler;

    @GetMapping("/gyms/crawl")
    public ResponseEntity<List<GymDTO>> crawlGyms() {
        List<GymDTO> gyms = kakaoGymCrawler.crawlAllGyms();
        return ResponseEntity.ok(gyms);
    }
}