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

    @PostMapping
    public ResponseEntity<String> saveGyms(@RequestBody List<Gym> gymData) {
        // gymData가 정상적으로 파싱되고 있는지 확인
        System.out.println("Received gym data: " + gymData);

        // 데이터 처리 후 저장 로직 추가
        return ResponseEntity.ok("헬스장 데이터가 성공적으로 저장되었습니다.");
    }
}
