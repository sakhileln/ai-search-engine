package za.auth.grpc.service;

import ai.search.auth.AuthServiceGrpc;
import ai.search.auth.LoginRequest;
import ai.search.auth.LoginResponse;
import ai.search.auth.ValidateTokenRequest;
import ai.search.auth.ValidateTokenResponse;
import ai.search.common.StandardResponse;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import za.auth.service.AuthService;
import za.auth.service.JwtService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for authentication operations.
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class AuthGrpcServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            log.debug("Processing gRPC login request for user: {}", request.getUsername());
            
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate tokens
            String accessToken = jwtService.generateToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(authentication);
            
            // Get user details
            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            
            // Build response
            LoginResponse response = LoginResponse.newBuilder()
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setTokenType("Bearer")
                    .setExpiresIn((long) jwtService.getJwtExpirationInMs() / 1000) // Convert to seconds
                    .setUserId(userDetails.getUsername())
                    .addAllRoles(roles)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error during gRPC login", e);
            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription("Invalid username or password")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void validateToken(ValidateTokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        try {
            String token = request.getToken();
            log.debug("Validating token via gRPC");
            
            if (jwtService.validateToken(token)) {
                String username = jwtService.getUsernameFromToken(token);
                List<String> roles = jwtService.getRolesFromToken(token);
                
                // Check if any required roles are specified and if the user has them
                boolean hasRequiredRoles = request.getRequiredRolesList().isEmpty() ||
                        roles.containsAll(request.getRequiredRolesList());
                
                ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                        .setValid(hasRequiredRoles)
                        .setUserId(username)
                        .addAllRoles(roles)
                        .build();
                
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onNext(ValidateTokenResponse.newBuilder()
                        .setValid(false)
                        .build());
                responseObserver.onCompleted();
            }
            
        } catch (Exception e) {
            log.error("Error validating token via gRPC", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error validating token")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void logout(com.google.protobuf.Empty request, StreamObserver<StandardResponse> responseObserver) {
        try {
            // In a real implementation, you would invalidate the token here
            log.debug("Processing gRPC logout request");
            
            StandardResponse response = StandardResponse.newBuilder()
                    .setSuccess(true)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error during gRPC logout", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error during logout")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void refreshToken(ai.search.auth.RefreshTokenRequest request, 
                           StreamObserver<ai.search.auth.RefreshTokenResponse> responseObserver) {
        try {
            String refreshToken = request.getRefreshToken();
            log.debug("Refreshing token via gRPC");
            
            if (jwtService.validateToken(refreshToken)) {
                String username = jwtService.getUsernameFromToken(refreshToken);
                String newAccessToken = jwtService.generateTokenFromUsername(username);
                
                ai.search.auth.RefreshTokenResponse response = ai.search.auth.RefreshTokenResponse.newBuilder()
                        .setAccessToken(newAccessToken)
                        .setRefreshToken(refreshToken) // Optionally issue a new refresh token
                        .setTokenType("Bearer")
                        .setExpiresIn((long) jwtService.getJwtExpirationInMs() / 1000)
                        .build();
                
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(Status.UNAUTHENTICATED
                        .withDescription("Invalid or expired refresh token")
                        .asRuntimeException());
            }
            
        } catch (Exception e) {
            log.error("Error refreshing token via gRPC", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error refreshing token")
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
