package za.auth.grpc;

import io.grpc.ServerInterceptor;
import io.grpc.health.v1.HealthGrpc;
import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import za.auth.grpc.interceptor.AuthGrpcInterceptor;
import za.auth.grpc.interceptor.LoggingGrpcInterceptor;

/**
 * Configuration class for gRPC server setup.
 * Registers global interceptors and configures the gRPC server.
 */
@Configuration
public class GrpcServerConfiguration {

    /**
     * Configures the logging interceptor with the highest precedence.
     */
    @Bean
    @GrpcGlobalServerInterceptor
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ServerInterceptor loggingServerInterceptor() {
        return new LoggingGrpcInterceptor();
    }

    /**
     * Configures the authentication interceptor.
     */
    @Bean
    @GrpcGlobalServerInterceptor
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ServerInterceptor authServerInterceptor(AuthGrpcInterceptor interceptor) {
        return interceptor;
    }

    /**
     * Configures the global interceptor configurer.
     */
    @Bean
    public GlobalServerInterceptorConfigurer globalInterceptorConfigurer() {
        return registry -> {
            // Register any additional global interceptors here
        };
    }

    /**
     * Enables gRPC server reflection for testing and debugging.
     * This should be disabled in production.
     */
    @Bean
    public GrpcServerConfigurer serverReflectionConfigurer() {
        return serverBuilder -> {
            // Enable gRPC server reflection
            serverBuilder.intercept(io.grpc.protobuf.services.ProtoReflectionService.newInstance());
        };
    }
}
