package io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);
}
