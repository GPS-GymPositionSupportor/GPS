package gps.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Value("${RESOURCE_PATH}")
    private String resourcePath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Resource Path: {}", resourcePath);
        String realPath = resourcePath.replace("\\", "/");

        registry.addResourceHandler(uploadPath)  // 가상 URL 패턴
                .addResourceLocations("file:///" + realPath + "/");

        registry.addResourceHandler("/image/**")
                .addResourceLocations("/WEB-INF/image/");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:8080",
                        "http://localhost:3000",  // 다른 도메인
                        "http://127.0.0.1:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)  // 중요: 쿠키/인증 허용
                .maxAge(3600);
    }

}
