package za.auth.service;

import za.auth.dto.AuthResponse;
import za.auth.dto.LoginRequest;
import za.auth.dto.SignupRequest;
import za.auth.entity.RefreshToken;
import za.auth.entity.User;
import za.auth.repository.RefreshTokenRepository;
import za.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false); // Email verification required
        user.setLocked(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        var jwtToken = jwtService.generateToken(new CustomUserDetails(savedUser));
        var refreshToken = generateRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwtToken = jwtService.generateToken(new CustomUserDetails(user));
        var refreshToken = generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    private RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setCreatedAt(LocalDateTime.now());

        return refreshTokenRepository.save(refreshToken);
    }

    // Add missing methods for controller compatibility
    public AuthResponse registerUser(SignupRequest request) {
        return signup(request);
    }

    public AuthResponse authenticateUser(LoginRequest request) {
        return login(request);
    }

    public AuthResponse refreshToken(String token) {
        // Stub: implement refresh logic as needed
        return AuthResponse.builder().accessToken("stub").refreshToken(token).build();
    }

    public AuthResponse logout(String token) {
        // Stub: implement logout logic as needed
        return AuthResponse.builder().accessToken("").refreshToken("").build();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User updateUser(String email, User userDetails) {
        // Stub: implement update logic as needed
        return userDetails;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void deleteUser(String email) {
        // Stub: implement delete logic as needed
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Helper class to wrap User entity for Spring Security
    private static class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {
        private final User user;

        public CustomUserDetails(User user) {
            this.user = user;
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return !user.isLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.isEnabled();
        }

        @Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return java.util.Collections
                    .singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
        }
    }
}
