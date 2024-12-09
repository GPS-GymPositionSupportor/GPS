package gps.base.config.Elastic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoMapConfig {
    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public String getKakaoApiKey() {
        return kakaoApiKey;
    }
}