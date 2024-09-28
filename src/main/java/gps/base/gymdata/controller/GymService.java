package gps.base.gymdata.controller;

import gps.base.gymdata.model.Gym;
import gps.base.gymdata.repository.GymRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GymService {

    @Autowired
    private GymRepository gymRepository;

    public void save(Gym gym) {
        gymRepository.save(gym);
    }
}
