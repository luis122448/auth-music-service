package pe.bbg.music.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.bbg.music.auth.entity.enums.SubscriptionTierEnum;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSubscriptionTierRequest {
    private SubscriptionTierEnum newTier; 
}
