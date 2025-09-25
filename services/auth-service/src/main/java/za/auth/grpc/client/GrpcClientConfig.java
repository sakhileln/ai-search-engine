package za.auth.grpc.client;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for gRPC clients.
 * Provides pre-configured gRPC channels for communicating with other services.
 */
@Slf4j
@Configuration
public class GrpcClientConfig {

    @Value("${grpc.client.search-service.host:localhost}")
    private String searchServiceHost;

    @Value("${grpc.client.search-service.port:6566}")
    private int searchServicePort;

    @Value("${grpc.client.data-service.host:localhost}")
    private String dataServiceHost;

    @Value("${grpc.client.data-service.port:6567}")
    private int dataServicePort;

    @Value("${grpc.client.max-inbound-message-size:10485760}") // 10MB default
    private int maxInboundMessageSize;

    @Value("${grpc.client.keepAliveTime:30s}")
    private String keepAliveTime;

    @Value("${grpc.client.keepAliveTimeout:10s}")
    private String keepAliveTimeout;

    @Bean(name = "searchServiceChannel")
    public Channel searchServiceChannel() {
        return createChannel(searchServiceHost, searchServicePort);
    }

    @Bean(name = "dataServiceChannel")
    public Channel dataServiceChannel() {
        return createChannel(dataServiceHost, dataServicePort);
    }

    private Channel createChannel(String host, int port) {
        log.info("Creating gRPC channel to {}:{}", host, port);
        
        return NettyChannelBuilder.forAddress(host, port)
                .usePlaintext() // For development only, use TLS in production
                .maxInboundMessageSize(maxInboundMessageSize)
                .keepAliveTime(parseDuration(keepAliveTime), TimeUnit.MILLISECONDS)
                .keepAliveTimeout(parseDuration(keepAliveTimeout), TimeUnit.MILLISECONDS)
                .enableRetry()
                .build();
    }

    private long parseDuration(String duration) {
        try {
            if (duration.endsWith("ms")) {
                return Long.parseLong(duration.substring(0, duration.length() - 2));
            } else if (duration.endsWith("s")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 1000;
            } else if (duration.endsWith("m")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 60 * 1000;
            } else {
                return Long.parseLong(duration);
            }
        } catch (Exception e) {
            log.warn("Invalid duration format: {}. Using default value.", duration, e);
            return 30000; // 30 seconds default
        }
    }
}
