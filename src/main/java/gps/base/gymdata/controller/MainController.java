package gps.base.gymdata.controller;

import gps.base.GymService;
import gps.base.gymdata.model.Gym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    private final GymService gymService;

    @Autowired
    public MainController(GymService gymService) {
        this.gymService = gymService;
    }

    // 헬스장 데이터를 가져오는 GET 요청
    @GetMapping("/gyms")
    public ResponseEntity<List<Gym>> getAllGyms() {
        List<Gym> gyms = gymService.getAllGyms();
        return ResponseEntity.ok(gyms);
    }

    // 헬스장 데이터를 저장하는 POST 요청
    @PostMapping("/gyms")
    public ResponseEntity<String> saveGyms(@RequestBody List<Gym> gyms) {
        gymService.saveGyms(gyms);
        return ResponseEntity.ok("헬스장 데이터가 성공적으로 저장되었습니다.");
    }
}