package gps.base.gymdata.controller;

import gps.base.gymdata.model.Gym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gyms")
public class GymController {
    @Autowired
    private GymService gymService;

    @PostMapping
    public ResponseEntity<String> saveGyms(@RequestBody List<Gym> gyms) {
        try {
            for (Gym gym : gyms) {
                if (gym.getGName() == null) {
                    return ResponseEntity.badRequest().body("gName 필드는 null일 수 없습니다.");
                }
                gymService.save(gym);
            }
            return ResponseEntity.ok("{\"message\":\"Success\"}"); // JSON 형식으로 응답
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"서버 오류: " + e.getMessage() + "\"}"); // JSON 형식으로 오류 응답
        }
    }
}
