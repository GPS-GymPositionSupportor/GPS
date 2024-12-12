package gps.base.rdb.service;

import gps.base.rdb.DTO.GymDTO;
import gps.base.rdb.error.ErrorCode;
import gps.base.rdb.error.exception.CustomException;
import gps.base.rdb.model.Authority;
import gps.base.rdb.model.Gym;
import gps.base.rdb.repository.GymRepository;
import gps.base.rdb.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GymService {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    // 체육관 저장
    @Transactional
    public Gym saveGym(Gym gym) {
        gym.setGCreatedAt(LocalDateTime.now());
        return gymRepository.save(gym);
    }


    public Map<String, Object> getGymDetailById(Long gymId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));

        Map<String, Object> gymDetail = convertGymToMap(gym);

        // 리뷰 정보가 필요한 경우
        List<Map<String, Object>> reviews = reviewService.getReviewWithMember(gymId);
        gymDetail.put("reviews", reviews);

        return gymDetail;
    }

    private Map<String, Object> convertGymToMap(Gym gym) {
        Map<String, Object> gymMap = new HashMap<>();
        gymMap.put("gymId", gym.getGymId());
        gymMap.put("gName", gym.getGName());
        gymMap.put("category", gym.getCategory());
        gymMap.put("address", gym.getAddress());
        gymMap.put("openHour", gym.getOpenHour());
        gymMap.put("homepage", gym.getHomepage());
        gymMap.put("phone", gym.getPhone());
        gymMap.put("rating", gym.getRating());
        gymMap.put("gLongitude", gym.getGLongitude());
        gymMap.put("gLatitude", gym.getGLatitude());

        return gymMap;
    }

    public List<GymDTO> findNearbyGyms(double userLat, double userLng) {
        return gymRepository.findGymsWithin3Km(userLat, userLng)
                .stream()
                .map(GymDTO::fromEntity)
                .collect(Collectors.toList());
    }


    // 모든 체육관 찾기
    public List<Gym> getAllGyms() {
        return gymRepository.findAll();
    }


    /**
     * 체육관 정보 수정
     * @param gymId 체육관 ID
     */
    @Transactional
    public GymDTO updateGym(Long gymId, GymDTO gymData) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));

        // 기본 정보 업데이트
        gym.setGName(gymData.getG_name());
        gym.setAddress(gymData.getAddress());
        gym.setPhone(gymData.getPhone());
        gym.setHomepage(gymData.getHomepage());
        gym.setOpenHour(gymData.getOpenHour());

        gym = gymRepository.save(gym);
        return convertToDTO(gym);
    }

    private GymDTO convertToDTO(Gym gym) {
        GymDTO dto = new GymDTO();
        dto.setGymId(gym.getGymId());
        dto.setG_name(gym.getGName());
        dto.setAddress(gym.getAddress());
        dto.setPhone(gym.getPhone());
        dto.setHomepage(gym.getHomepage());
        dto.setOpenHour(gym.getOpenHour());
        return dto;
    }


    /**
     * 체육관 삭제 (소프트 삭제)
     * @param gymId 체육관 ID
     */
    @Transactional
    public void deleteGym(Long gymId, Long userId, Authority authority) {
        Gym gym = validateGymAndImage(gymId, userId, null, authority);

        // 연관된 이미지 삭제
        imageRepository.deleteByGymId(gymId);

        // 체육관 삭제
        gymRepository.delete(gym);
        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "체육관이 삭제되었습니다.");
    }

    // 체욱관 & 이미지 검증
    public Gym validateGymAndImage(Long gymId, Long userId, MultipartFile file, Authority authority) {
        // 체육관 존재 여부
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));

        // 작성자 확인
        if (authority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.WRITER_DOES_NOT_MATCH);
        }

        // 이미지가 있는 경우 이미지 검증
        if (file != null && !file.isEmpty()) {
            validateImageFile(file);
        }
        return gym;

    }

    // 이미지 파일 검증 메서드
    private void validateImageFile(MultipartFile file) {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.NO_FILE_PROVIDED);
        }

        // 파일 형식 검증
        String contentType = file.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            throw new CustomException(ErrorCode.NOT_IMAGE_FILE);
        }

        // 파일 크기 검증 (예: 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEED);
        }
    }

    // 이미지 타입 검증
    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }

    /**
     * 위도/경도 유효성 검사
     */
    private void validateCoordinate(double coordinate, String type) {
        if (type.equals("위도") && (coordinate < -90 || coordinate > 90)) {
            throw new IllegalArgumentException("위도는 -90에서 90 사이의 값이어야 합니다.");
        }
        if (type.equals("경도") && (coordinate < -180 || coordinate > 180)) {
            throw new IllegalArgumentException("경도는 -180에서 180 사이의 값이어야 합니다.");
        }
    }

    // 체육관 찾기 (부분 일치)
    public List<Gym> searchGymsByName(String name) {
        return gymRepository.findBygNameContaining(name);
    }


    public Page<Gym> getGyms(PageRequest pageRequest) {
        return gymRepository.findAll(pageRequest);
    }
}
