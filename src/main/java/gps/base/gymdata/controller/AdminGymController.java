package gps.base.gymdata.controller;

import gps.base.gymdata.model.Gym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping("/api/admin/gyms")
public class AdminGymController {
    @Autowired
    private GymService gymService;

    @PostMapping("/register")
    public ResponseEntity<String> registerGym(
            @RequestParam("gName") String gName,
            @RequestParam("address") String address,
            @RequestParam("gLongitude") Double gLongitude,
            @RequestParam("gLatitude") Double gLatitude,
            @RequestParam("information") String information,
            @RequestParam("gymImage") MultipartFile gymImage) {

        try {
            Gym gym = new Gym();
            gym.setGName(gName);
            gym.setAddress(address);
            gym.setGLongitude(gLongitude);
            gym.setGLatitude(gLatitude);
            gym.setInformation(information);
            gym.setGymImage(Arrays.toString(gymImage.getBytes())); // 이미지 파일을 byte 배열로 변환

            gymService.save(gym); // 체육관 정보 저장
            return ResponseEntity.ok("{\"message\":\"체육관이 등록되었습니다.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"서버 오류: " + e.getMessage() + "\"}");
        }
    }
}
