package gps.base.search.ElasticService;


import gps.base.search.ElasticConfig.KakaoMapConfig;
import gps.base.search.ElasticDTO.LocationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private final KakaoMapConfig kakaoMapConfig;
    private final RestTemplate restTemplate;
    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";

    public LocationDTO getCurrentLocation() {
        try {
            // 카카오맵 API를 통한 현재 위치 조회
            String apiKey = kakaoMapConfig.getKakaoApiKey();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + apiKey);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            // API 호출 및 응답 처리
            ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(
                    KAKAO_API_URL,
                    HttpMethod.GET,
                    entity,
                    KakaoApiResponse.class
            );

            // 응답에서 위치 정보 추출 및 LocationDTO 반환
            if (response.getBody() != null && response.getBody().getLatitude() != 0) {
                return new LocationDTO(
                        response.getBody().getLatitude(),
                        response.getBody().getLongitude()
                );
            }

            throw new RuntimeException("Failed to get location from Kakao API");
        } catch (Exception e) {
            log.error("Failed to get current location: ", e);
            throw new RuntimeException("Failed to get current location", e);
        }
    }

    // Kakao API 응답을 매핑하기 위한 내부 클래스
    private static class KakaoApiResponse {
        private double latitude;
        private double longitude;

        // Getters and setters
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}