package za.auth.grpc.interceptor;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.auth.service.JwtService;

import java.util.List;

/**
 * gRPC server interceptor for handling authentication and authorization.
 * Validates JWT tokens in gRPC metadata and sets up security context.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGrpcInterceptor implements ServerInterceptor {

    private static final String BEARER_TYPE = "Bearer";
    private static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = 
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    private static final Context.Key<String> USERNAME_CTX_KEY = Context.key("username");
    private static final Context.Key<List<String>> ROLES_CTX_KEY = Context.key("roles");

    private final JwtService jwtService;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        
        String method = call.getMethodDescriptor().getFullMethodName();
        log.debug("Intercepting gRPC call: {}", method);

        // Skip auth for health checks and reflection
        if (method.startsWith("grpc.health")) {
            return next.startCall(call, headers);
        }

        try {
            // Extract token from metadata
            String token = extractToken(headers);
            if (token == null) {
                throw Status.UNAUTHENTICATED
                        .withDescription("Authorization token is missing")
                        .asRuntimeException();
            }

            // Validate token
            if (!jwtService.validateToken(token)) {
                throw Status.UNAUTHENTICATED
                        .withDescription("Invalid or expired token")
                        .asRuntimeException();
            }

            // Extract user info
            String username = jwtService.getUsernameFromToken(token);
            List<String> roles = jwtService.getRolesFromToken(token);

            // Add user info to context
            Context context = Context.current()
                    .withValue(USERNAME_CTX_KEY, username)
                    .withValue(ROLES_CTX_KEY, roles);

            // Check if the user has required roles for this method
            if (!hasRequiredRoles(method, roles)) {
                throw Status.PERMISSION_DENIED
                        .withDescription("Insufficient permissions")
                        .asRuntimeException();
            }

            log.debug("Authenticated gRPC call - user: {}, method: {}", username, method);
            return Contexts.interceptCall(context, call, headers, next);

        } catch (StatusRuntimeException e) {
            log.error("gRPC auth error: {} - {}", e.getStatus(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during gRPC auth", e);
            throw Status.INTERNAL
                    .withDescription("Internal server error during authentication")
                    .withCause(e)
                    .asRuntimeException();
        }
    }

    private String extractToken(Metadata headers) {
        String authHeader = headers.get(AUTHORIZATION_METADATA_KEY);
        if (authHeader == null) {
            return null;
        }

        if (!authHeader.startsWith(BEARER_TYPE)) {
            throw Status.UNAUTHENTICATED
                    .withDescription("Invalid authorization header format")
                    .asRuntimeException();
        }

        return authHeader.substring(BEARER_TYPE.length()).trim();
    }

    private boolean hasRequiredRoles(String method, List<String> userRoles) {
        // TODO: Implement role-based access control (RBAC) based on method
        // This is a simplified example - in a real application, you would have a more sophisticated
        // way to map methods to required roles, possibly using annotations or a configuration file.
        
        // For now, we'll just allow all authenticated users to access all methods
        // In a real implementation, you would check if any of the user's roles match the required roles
        // for the given method.
        return true;
    }
    
    /**
     * Gets the current authenticated username from the gRPC context.
     * @return The username or null if not authenticated
     */
    public static String getCurrentUsername() {
        return USERNAME_CTX_KEY.get();
    }
    
    /**
     * Gets the current user's roles from the gRPC context.
     * @return List of roles or empty list if not authenticated
     */
    public static List<String> getCurrentUserRoles() {
        return ROLES_CTX_KEY.get();
    }
}
