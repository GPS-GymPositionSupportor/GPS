package gps.base.service;

import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBasedSSOService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${SSO_TOKEN_SECRET}")
    private String secret;

    @Value("${SSO_TOKEN_EXPIRATION}")
    private Long expiration;

    public TokenBasedSSOService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // SSO 토큰 생성
    public String createSSOToken(Member member) {
        Map<String, Object> data = new HashMap<>();
        data.put("userID", member.getUserId());
        data.put("providerType", member.getProviderType());
        data.put("providerId", member.getProviderId());
        data.put("email", member.getEmail());

        String token = Jwts.builder()
                .setClaims(data)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        // Redis 에 토큰 저장
        String key = "sso:token:" + member.getUserId();
        redisTemplate.opsForValue().set(key, token, expiration, TimeUnit.SECONDS);

        return token;
    }

    // SSO 토큰 검증
    public Claims validateSSOToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            // Redis 에서 토큰 검증
            String userId = claims.get("userID", String.class);
            String storedToken = redisTemplate.opsForValue().get("sso:token:" + userId);

            if (storedToken != null && storedToken.equals(token)) {
                return claims;
            }
            throw new CustomException(ErrorCode.NONE_TOKEN);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public void invalidateToken(String userId) {
        redisTemplate.delete("sso:token:" + userId);
    }
}
