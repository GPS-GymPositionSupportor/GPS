package gps.base.service;

import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
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

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public TokenBasedSSOService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // SSO 토큰 생성
    public String createSSOToken(Member member) {
        Map<String, Object> data = new HashMap<>();
        data.put("userID", member.getUserId());
        data.put("providerType", member.getProviderType().toString());
        data.put("providerId", member.getProviderId());
        if(member.getEmail() != null) {
            data.put("email", member.getEmail());
        }

        String token = Jwts.builder()
                .setClaims(data)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Redis 에 토큰 저장
        String key = "sso:token:" + member.getUserId().toString();
        redisTemplate.opsForValue().set(key, token, expiration, TimeUnit.SECONDS);

        return token;
    }


    // SSO 토큰 검증
    public Claims validateSSOToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        Integer userIdInt = claims.get("userID", Integer.class);
        String userId = userIdInt.toString();
        String storedToken = redisTemplate.opsForValue().get("sso:token:" + userId);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return claims;
    }


    // 토큰 무효화 (로그아웃)
    public void invalidateToken(String userId) {
        String key = "sso:token:" + userId;
        redisTemplate.delete(key);
    }
}
