package gps.base.service;

import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Authority;
import gps.base.model.Gym;
import gps.base.model.Review;
import gps.base.repository.GymRepository;
import gps.base.repository.ImageRepository;
import gps.base.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
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


    // 모든 체육관 찾기
    public List<Gym> getAllGyms() {
        return gymRepository.findAll();
    }


    /**
     * 체육관 정보 수정
     * @param gymId 체육관 ID
     * @param gymDetails 수정할 체육관 정보
     * @return 수정된 체육관 정보를 Map 형태로 반환
     */
    @Transactional
    public Map<String, Object> updateGym(Long gymId, Gym gymDetails) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("체육관을 찾을 수 없습니다. ID: " + gymId));

        // Null 체크 및 유효성 검사
        if (gymDetails.getGName() != null && !gymDetails.getGName().trim().isEmpty()) {
            gym.setGName(gymDetails.getGName());
        }

        if (gymDetails.getAddress() != null && !gymDetails.getAddress().trim().isEmpty()) {
            gym.setAddress(gymDetails.getAddress());
        }

        // 위도/경도는 0이 아닌 경우에만 업데이트
        if (gymDetails.getGLongitude() != 0) {
            validateCoordinate(gymDetails.getGLongitude(), "경도");
            gym.setGLongitude(gymDetails.getGLongitude());
        }

        if (gymDetails.getGLatitude() != 0) {
            validateCoordinate(gymDetails.getGLatitude(), "위도");
            gym.setGLatitude(gymDetails.getGLatitude());
        }

        // 선택적 필드들 업데이트
        if (gymDetails.getOpenHour() != null) {
            gym.setOpenHour(gymDetails.getOpenHour());
        }

        if (gymDetails.getHomepage() != null) {
            gym.setHomepage(gymDetails.getHomepage());
        }

        if (gymDetails.getPhone() != null) {
            gym.setPhone(gymDetails.getPhone());
        }

        if (gymDetails.getCategory() != null) {
            gym.setCategory(gymDetails.getCategory());
        }

        Gym updatedGym = gymRepository.save(gym);
        return convertGymToMap(updatedGym);  // 위에서 만든 convertGymToMap 메서드 재사용
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
