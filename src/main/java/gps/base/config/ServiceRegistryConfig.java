package gps.base.config;

import gps.base.component.ServiceInfo;
import gps.base.component.ServiceRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "service.registry")
public class ServiceRegistryConfig {

    @Getter
    private List<ServiceInfo> registerServices;

    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistry(registerServices);
    }

}
