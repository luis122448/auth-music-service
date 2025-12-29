package pe.bbg.music.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.bbg.music.auth.dto.*;
import pe.bbg.music.auth.entity.SubscriptionTier;
import pe.bbg.music.auth.entity.User;
import pe.bbg.music.auth.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .country(request.getCountry())
                .subscriptionTier(SubscriptionTier.FREE) // Default
                .avatarUrl("https://ui-avatars.com/api/?name=" + request.getUsername()) // Default placeholder
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .user(mapToUserResponse(user))
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .user(mapToUserResponse(user))
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = repository.findByUsername(username).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                return AuthResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .user(mapToUserResponse(user))
                        .build();
            }
        }
        throw new IllegalArgumentException("Invalid refresh token");
    }

    public UserResponse getCurrentUser() {
        User user = getAuthenticatedUser();
        return mapToUserResponse(user);
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = getAuthenticatedUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    public UserResponse changeRole(UUID userId, ChangeRoleRequest request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(request.getNewRole());
        repository.save(user);
        return mapToUserResponse(user);
    }

    public UserResponse changeSubscriptionTier(UUID userId, ChangeSubscriptionTierRequest request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setSubscriptionTier(request.getNewTier());
        repository.save(user);
        return mapToUserResponse(user);
    }

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .country(user.getCountry())
                .subscriptionTier(user.getSubscriptionTier())
                .build();
    }
}
