package pe.bbg.music.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.bbg.music.auth.dto.*;
import pe.bbg.music.auth.service.AuthService;

import java.util.UUID;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {

    private final AuthService service;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided details.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody RegisterRequest request
    ) {
        AuthResponse response = service.register(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "User registered successfully")
        );
    }

    @Operation(summary = "Authenticate user", description = "Validates credentials and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(
            @RequestBody AuthRequest request
    ) {
        AuthResponse response = service.authenticate(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Authentication successful")
        );
    }

    @Operation(summary = "Refresh token", description = "Generates a new access token using a valid refresh token.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        AuthResponse response = service.refreshToken(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Token refreshed successfully")
        );
    }

    @Operation(summary = "Validate token", description = "Checks if the provided JWT token is valid and not expired.")
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken() {
        return ResponseEntity.ok(
                ApiResponse.success("Valid token", "Token validated successfully")
        );
    }

    @Operation(summary = "Get current user info", description = "Retrieves details of the currently authenticated user.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        return ResponseEntity.ok(
                ApiResponse.success(service.getCurrentUser(), "User details retrieved successfully")
        );
    }

    @Operation(summary = "Change password", description = "Updates the password for the current user.")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {
        service.changePassword(request);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Password updated successfully")
        );
    }

    @Operation(summary = "Change user role", description = "Updates the role of a specific user (Admin only).")
    @PutMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserResponse>> changeRole(
            @PathVariable UUID userId,
            @RequestBody ChangeRoleRequest request
    ) {
        // In a real scenario, check here if currentUser is ADMIN
        return ResponseEntity.ok(
                ApiResponse.success(service.changeRole(userId, request), "User role updated successfully")
        );
    }


    @Operation(summary = "Change subscription tier", description = "Updates the subscription tier of the current user.")
    @PutMapping("/{userId}/subscription-tier")
    public ResponseEntity<ApiResponse<UserResponse>> changeSubscriptionTier(
            @PathVariable UUID userId,
            @RequestBody ChangeSubscriptionTierRequest request
    ) {
        // In a real scenario, check here if currentUser is authorized
        return ResponseEntity.ok(
                ApiResponse.success(service.changeSubscriptionTier(userId, request), "User subscription tier updated successfully")
        );
    }
}
