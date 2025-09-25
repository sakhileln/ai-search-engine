package za.auth.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.protobuf.services.HealthStatusManager;
import io.grpc.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port:6565}")
    private int grpcServerPort;

    @Autowired(required = false)
    private GrpcServiceRegistry grpcServiceRegistry;

    @Bean
    public HealthStatusManager healthStatusManager() {
        return new HealthStatusManager();
    }

    @Bean
    public Server grpcServer(HealthStatusManager healthStatusManager) throws IOException {
        log.info("Starting gRPC server on port: {}", grpcServerPort);
        
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(grpcServerPort)
                .addService(healthStatusManager.getHealthService())
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(10, TimeUnit.SECONDS)
                .permitKeepAliveTime(10, TimeUnit.SECONDS)
                .permitKeepAliveWithoutCalls(true);

        // Register all gRPC services
        if (grpcServiceRegistry != null) {
            grpcServiceRegistry.registerServices(serverBuilder);
        } else {
            log.warn("No gRPC services found to register");
        }

        Server server = serverBuilder.build();
        server.start();
        
        // Set initial health status
        healthStatusManager.setStatus("", HealthCheckResponse.ServingStatus.SERVING);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC server...");
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Error shutting down gRPC server", e);
            }
        }));
        
        return server;
    }
}
