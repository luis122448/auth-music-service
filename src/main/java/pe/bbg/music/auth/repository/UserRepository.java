package pe.bbg.music.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.bbg.music.auth.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
