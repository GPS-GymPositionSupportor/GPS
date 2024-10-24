package gps.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path")
    private String uploadPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 실제 경로 로깅
        log.info("Actual upload path: {}", uploadPath);

        try {
            // 경로 정규화
            Path path = Paths.get(uploadPath).toAbsolutePath().normalize();
            String resourceLocation = path.toUri().toString();

            log.info("Resource location: {}", resourceLocation);

            registry.addResourceHandler("/images/**")
                    .addResourceLocations(resourceLocation)
                    .setCachePeriod(3600)
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver() {
                        @Override
                        protected Resource getResource(String resourcePath, Resource location) throws IOException {
                            Resource resource = super.getResource(resourcePath, location);
                            if (resource == null || !resource.exists()) {
                                log.debug("Resource not found: {}", resourcePath);
                                return null;
                            }
                            return resource;
                        }
                    });
        } catch (Exception e) {
            log.error("Failed to configure resource handler", e);
        }
    }
}
