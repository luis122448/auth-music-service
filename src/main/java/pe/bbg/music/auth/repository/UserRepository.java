package pe.bbg.music.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.bbg.music.auth.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);
}
