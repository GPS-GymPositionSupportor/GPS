package gps.base.component;

import gps.base.config.ServiceRegistryConfig;

import java.util.List;

public class ServiceRegistry {
    private final List<ServiceInfo> registeredServices;

    public ServiceRegistry(List<ServiceInfo> registeredServices) {
        this.registeredServices = registeredServices;
    }

    public ServiceInfo getServiceInfo(String name) {
        return registeredServices.stream()
                .filter(service -> service.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
