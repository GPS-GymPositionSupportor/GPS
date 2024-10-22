package gps.base.service;

import gps.base.model.Gym;
import gps.base.repository.GymRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GymService {

    @Autowired
    private GymRepository gymRepository;

    
    // 체육관 저장
    @Transactional
    public Gym saveGym(Gym gym) {
        gym.setGCreatedAt(LocalDateTime.now());
        return gymRepository.save(gym);
    }

    // gym id값으로 체육관 찾기
    public Gym getGymById(Long gymId) {
        return gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 체육관을 찾을 수 없습니다. ID : " + gymId));
    }

    // 모든 체육관 찾기
    public List<Gym> getAllGyms() {
        return gymRepository.findAll();
    }
    
    // 체육관 정보 수정
    @Transactional
    public Gym updateGym(Long gymId, Gym gymDetails) {
        Gym gym = getGymById(gymId);

        gym.setGName(gymDetails.getGName());
        gym.setAddress(gymDetails.getAddress());
        gym.setGLongitude(gymDetails.getGLongitude());
        gym.setGLatitude(gymDetails.getGLatitude());
        gym.setInformation(gymDetails.getInformation());

        return gymRepository.save(gym);
    }

    // 체육관 삭제
    @Transactional
    public void deleteGym(Long gymId) {
        Gym gym = getGymById(gymId);
        gym.setGDeletedAt(LocalDateTime.now());
        gym.setGDeletedBy("Admin"); // Admin 관련 코드 작성 후에 코드 개선 요망
        gymRepository.save(gym);
    }

    // 체육관 찾기 (부분 일치)
    public List<Gym> searchGymsByName(String name) {
        return gymRepository.findBygNameContaining(name);
    }
    
    
}
