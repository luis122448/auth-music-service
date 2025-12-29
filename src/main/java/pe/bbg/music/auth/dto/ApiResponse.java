package pe.bbg.music.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private ResponseStatusEnum status; // Now using restricted ENUM
    private String message;
    private T data;

    private String logUser;
    private String logMessage;
    private LocalDateTime logDate;

    // Getter returning Optional to avoid NPE in Java clients (if used internally)
    public Optional<T> data() {
        return Optional.ofNullable(data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(ResponseStatusEnum.SUCCESS)
                .message(message)
                .data(data)
                .logDate(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String logMessage, String logUser) {
        return ApiResponse.<T>builder()
                .status(ResponseStatusEnum.ERROR)
                .message(message)
                .logMessage(logMessage)
                .logUser(logUser)
                .logDate(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> warning(String message) {
        return ApiResponse.<T>builder()
                .status(ResponseStatusEnum.WARNING)
                .message(message)
                .logDate(LocalDateTime.now())
                .build();
    }
}
