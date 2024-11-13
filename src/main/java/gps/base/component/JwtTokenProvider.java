package gps.base.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private String tokenSecret;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    // 토큰 유효 시간 설정
    @Value("${JWT_TOKEN_VALIDATION_INSECONDS}")
    private long tokenValidityInSeconds;


    public JwtTokenProvider(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }


    /**
     * JWT 토큰 생성 메서드
     * @param email
     * @param authority
     * @return 생성된 JWT 토큰
     */

    public String createToken(String email, String authority) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("authority", authority);

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)  // 정보 저장
                .setIssuedAt(now)   // 토큰 발행 시간
                .setExpiration(validity)    // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  // 암호화 알고리즘, secret 값 설정
                .compact();
    }

    /**
     * 토큰에서 이메일 추출
     * @param token JWT 토큰
     * @return 토큰에서 추출한 사용자 이메일
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰 유효성 검증
     * @param token
     * @return 토큰 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth", String[].class))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }


}
