package gps.base;

import gps.base.gymdata.model.Gym;
import gps.base.gymdata.repository.GymRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymService {

    private final GymRepository gymRepository;

    @Autowired
    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    public List<Gym> getAllGyms() {
        return gymRepository.findAll();
    }

    public void saveGyms(List<Gym> gyms) {
        gymRepository.saveAll(gyms);
    }
}