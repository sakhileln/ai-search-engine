package za.auth.grpc.health;

import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;

/**
 * gRPC Health Service implementation that integrates with Spring Boot Actuator's health endpoint.
 * Provides health status information for gRPC clients and load balancers.
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class GrpcHealthService extends HealthGrpc.HealthImplBase {

    private final HealthEndpoint healthEndpoint;

    @Override
    public void check(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        String service = request.getService();
        HealthCheckResponse.ServingStatus status;

        try {
            if (service == null || service.isEmpty()) {
                // Overall application health
                status = healthEndpoint.health().getStatus() == Status.UP
                        ? HealthCheckResponse.ServingStatus.SERVING
                        : HealthCheckResponse.ServingStatus.NOT_SERVING;
            } else {
                // Specific service health check
                org.springframework.boot.actuate.health.Health health = healthEndpoint.healthForComponent(service);
                status = health.getStatus() == Status.UP
                        ? HealthCheckResponse.ServingStatus.SERVING
                        : HealthCheckResponse.ServingStatus.NOT_SERVING;
            }
        } catch (Exception e) {
            log.error("Health check failed for service: " + service, e);
            status = HealthCheckResponse.ServingStatus.NOT_SERVING;
        }

        responseObserver.onNext(HealthCheckResponse.newBuilder()
                .setStatus(status)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void watch(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        // For simple implementation, we just return the current status
        // In a real implementation, you might want to implement proper streaming
        check(request, responseObserver);
    }
}
