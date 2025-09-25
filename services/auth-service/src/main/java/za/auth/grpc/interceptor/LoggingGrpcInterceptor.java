package za.auth.grpc.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * gRPC server interceptor for logging and monitoring gRPC calls.
 * Adds request/response logging, error handling, and request correlation.
 */
@Slf4j
@Component
public class LoggingGrpcInterceptor implements ServerInterceptor {
    
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String METHOD_KEY = "method";
    
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        
        // Generate a unique request ID if not present
        String requestId = headers.get(Metadata.Key.of(REQUEST_ID_KEY, Metadata.ASCII_STRING_MARSHALLER));
        if (requestId == null || requestId.isEmpty()) {
            requestId = "gRPC-" + UUID.randomUUID().toString();
        }
        
        final String methodName = call.getMethodDescriptor().getFullMethodName();
        final String finalRequestId = requestId;
        
        // Log the incoming request
        log.info("gRPC call started - method: {}, requestId: {}", methodName, requestId);
        
        // Add request context to MDC for logging
        MDC.put(REQUEST_ID_KEY, requestId);
        MDC.put(METHOD_KEY, methodName);
        
        // Create a new server call that wraps the original
        ServerCall<ReqT, RespT> wrappedCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendMessage(RespT message) {
                log.debug("Sending response - method: {}, requestId: {}", methodName, finalRequestId);
                super.sendMessage(message);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                if (status.isOk()) {
                    log.info("gRPC call completed - method: {}, requestId: {}, status: {}", 
                            methodName, finalRequestId, status);
                } else {
                    log.error("gRPC call failed - method: {}, requestId: {}, status: {}, error: {}", 
                            methodName, finalRequestId, status.getCode(), status.getDescription(), 
                            status.getCause());
                }
                super.close(status, trailers);
            }
        };
        
        // Forward the call to the next interceptor/handler
        try {
            return next.startCall(wrappedCall, headers);
        } catch (Exception e) {
            log.error("Error processing gRPC call - method: {}, requestId: {}", methodName, finalRequestId, e);
            throw e;
        } finally {
            // Clean up MDC
            MDC.remove(REQUEST_ID_KEY);
            MDC.remove(METHOD_KEY);
        }
    }
}
