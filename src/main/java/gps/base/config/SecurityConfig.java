package gps.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated()  // 모든 요청을 인증된 사용자만 허용
                )
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/login")  // 커스텀 로그인 페이지 설정
                                .defaultSuccessUrl("/main", true)  // 로그인 성공 후 리다이렉트할 URL
                );
        return http.build();
    }
}