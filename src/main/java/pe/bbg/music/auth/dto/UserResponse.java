package pe.bbg.music.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.bbg.music.auth.entity.enums.SubscriptionTierEnum;
import pe.bbg.music.auth.entity.enums.UserRoleEnum;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private UserRoleEnum role;
    private String avatarUrl;
    private String country;
    private SubscriptionTierEnum subscriptionTier;
}
