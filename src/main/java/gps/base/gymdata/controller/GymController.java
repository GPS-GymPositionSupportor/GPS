package gps.base.gymdata.controller;

import gps.base.gymdata.repository.GymRepository;
import gps.base.gymdata.model.Gym;
import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
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
        for(Gym gym : gyms) {
            gymService.save(gym);
        }
        return ResponseEntity.ok("Sucess");
    }
}
