package gps.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoGymService {
    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate;

    public KakaoGymService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getGymData(String region) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", region + " 헬스장")
                .queryParam("size", 15) // 한 번에 최대 15개의 결과를 요청
                .build().toUri();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "KakaoAK " + kakaoApiKey);

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response;
    }
}