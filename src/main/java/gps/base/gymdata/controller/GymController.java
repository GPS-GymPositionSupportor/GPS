package gps.base.gymdata.controller;

import gps.base.gymdata.repository.GymRepository;
import gps.base.gymdata.model.Gym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gyms")
public class GymController {
    @Autowired
    private GymRepository gymRepository;

    @PostMapping
    public List<Gym> saveGyms(@RequestBody List<Gym> gyms) {
        return gymRepository.saveAll(gyms);
    }

    @GetMapping
    public List<Gym> getAllGyms() {
        return gymRepository.findAll();
    }
}
