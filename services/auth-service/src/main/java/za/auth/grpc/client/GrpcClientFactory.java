package za.auth.grpc.client;

import ai.search.auth.AuthServiceGrpc;
import ai.search.search.SearchServiceGrpc;
import ai.search.data.DataServiceGrpc;
import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Factory for creating gRPC client stubs.
 * Provides pre-configured stubs for all gRPC services.
 */
@Configuration
public class GrpcClientFactory {

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub(
            @Qualifier("authServiceChannel") Channel channel) {
        return AuthServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public AuthServiceGrpc.AuthServiceFutureStub authServiceFutureStub(
            @Qualifier("authServiceChannel") Channel channel) {
        return AuthServiceGrpc.newFutureStub(channel);
    }

    @Bean
    public SearchServiceGrpc.SearchServiceBlockingStub searchServiceBlockingStub(
            @Qualifier("searchServiceChannel") Channel channel) {
        return SearchServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public SearchServiceGrpc.SearchServiceFutureStub searchServiceFutureStub(
            @Qualifier("searchServiceChannel") Channel channel) {
        return SearchServiceGrpc.newFutureStub(channel);
    }

    @Bean
    public DataServiceGrpc.DataServiceBlockingStub dataServiceBlockingStub(
            @Qualifier("dataServiceChannel") Channel channel) {
        return DataServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public DataServiceGrpc.DataServiceFutureStub dataServiceFutureStub(
            @Qualifier("dataServiceChannel") Channel channel) {
        return DataServiceGrpc.newFutureStub(channel);
    }
}
