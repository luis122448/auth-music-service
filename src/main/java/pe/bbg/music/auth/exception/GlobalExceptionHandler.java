package pe.bbg.music.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.bbg.music.auth.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        String currentUser = getCurrentUsername();
        ApiResponse<Object> response = ApiResponse.error(
                "Invalid credentials. Please verify your username and password.",
                ex.getMessage(),
                currentUser
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        String currentUser = getCurrentUsername();
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                "Validation error or resource not found",
                currentUser
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            io.jsonwebtoken.security.SignatureException.class,
            io.jsonwebtoken.ExpiredJwtException.class,
            io.jsonwebtoken.MalformedJwtException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleJwtExceptions(Exception ex) {
        String currentUser = getCurrentUsername();
        String userMessage = "Invalid or expired token. Please login again.";
        
        if (ex instanceof io.jsonwebtoken.ExpiredJwtException) {
            userMessage = "The token has expired. Please refresh it or login again.";
        }

        ApiResponse<Object> response = ApiResponse.error(
                userMessage,
                ex.getMessage(),
                currentUser
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        String currentUser = getCurrentUsername();
        String technicalMessage = ex.getMessage();
        String userMessage = "Invalid input format or invalid value for fields (e.g., Role, SubscriptionTier).";

        if (technicalMessage != null && technicalMessage.contains("UserRole")) {
            userMessage = "Invalid role provided. Accepted values are: [USER, ADMIN]";
        } else if (technicalMessage != null && technicalMessage.contains("SubscriptionTier")) {
            userMessage = "Invalid subscription tier provided. Accepted values are: [FREE, PREMIUM]";
        }

        ApiResponse<Object> response = ApiResponse.error(
                userMessage,
                technicalMessage,
                currentUser
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        String currentUser = getCurrentUsername();
        ApiResponse<Object> response = ApiResponse.error(
                "An internal server error occurred.",
                ex.getMessage(),
                currentUser
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper to get current user if authenticated
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return "ANONYMOUS";
    }
}
