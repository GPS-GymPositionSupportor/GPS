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
    }
}
