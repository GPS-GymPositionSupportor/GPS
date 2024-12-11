package gps.base.search.ElasticConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoMapConfig {


    private String kakaoApiKey;

    public String getKakaoApiKey() {
        return kakaoApiKey;
    }
}