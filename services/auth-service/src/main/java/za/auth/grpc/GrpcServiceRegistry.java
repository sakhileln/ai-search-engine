package za.auth.grpc;

import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServerServiceDefinition.Builder;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Registry for gRPC services that will be exposed by the gRPC server.
 * Automatically discovers and registers all gRPC services (implementing BindableService)
 * that are Spring beans.
 */
@Slf4j
@Component
public class GrpcServiceRegistry {

    @Autowired
    private ApplicationContext applicationContext;

    private final List<BindableService> services = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Find all gRPC services in the application context
        Map<String, BindableService> grpcServices = applicationContext.getBeansOfType(BindableService.class);
        
        if (!grpcServices.isEmpty()) {
            log.info("Discovered {} gRPC service(s) to register", grpcServices.size());
            services.addAll(grpcServices.values());
            
            // Log the discovered services
            grpcServices.values().forEach(service -> 
                log.info("Registered gRPC service: {}", service.getClass().getSimpleName())
            );
        } else {
            log.warn("No gRPC services found to register. Did you forget to annotate them with @GrpcService?");
        }
    }

    /**
     * Registers all discovered gRPC services with the provided ServerBuilder.
     *
     * @param serverBuilder The ServerBuilder to register services with
     */
    public void registerServices(ServerBuilder<?> serverBuilder) {
        services.forEach(service -> {
            ServerServiceDefinition serviceDefinition = service.bindService();
            serverBuilder.addService(serviceDefinition);
            log.info("Registered gRPC service: {}", serviceDefinition.getServiceDescriptor().getName());
        });
    }

    /**
     * Gets the list of registered gRPC services.
     *
     * @return List of registered gRPC services
     */
    public List<BindableService> getServices() {
        return new ArrayList<>(services);
    }
}
