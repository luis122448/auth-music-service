package pe.bbg.music.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.bbg.music.auth.entity.User;
import pe.bbg.music.auth.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            log.info("Admin user not found. Creating default admin user...");
            
            User admin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode("admin")) // Default password
                    .email("admin@bbg.pe")
                    .role(pe.bbg.music.auth.entity.UserRole.ADMIN)
                    .country("PE")
                    .subscriptionTier(pe.bbg.music.auth.entity.SubscriptionTier.PREMIUM)
                    .avatarUrl("https://ui-avatars.com/api/?name=Admin")
                    .build();
            
            userRepository.save(admin);
            log.info("Default admin user created successfully.");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
