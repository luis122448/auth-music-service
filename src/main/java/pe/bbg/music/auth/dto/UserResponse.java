package pe.bbg.music.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.bbg.music.auth.entity.SubscriptionTier;
import pe.bbg.music.auth.entity.UserRole;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private UserRole role;
    private String avatarUrl;
    private String country;
    private SubscriptionTier subscriptionTier;
}
