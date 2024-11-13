package gps.base.component;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class GlobalSessionManager {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long SESSION_TIMEOUT = 3600L;  // 1시간

    public void createSession(String token, String userId) {
        redisTemplate.opsForValue().set(
                "session:" + token,
                userId,
                Duration.ofSeconds(SESSION_TIMEOUT)
        );
    }

    public boolean validateSession(String token) {
        String userId = redisTemplate.opsForValue().get("session:" + token);
        return userId != null;
    }

    public void removeSession(String token) {
        redisTemplate.delete("session:" + token);
    }
}
