package gps.base.DTO;

import java.util.Map;

public class KaKaoResponse implements Oauth2Response {

    private final Map<String, Object> attribute;

    public KaKaoResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
