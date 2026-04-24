package io.github.lucas_goncalves_tech.tech_equipment_manager.support;

import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.Email;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.User;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.UserRole;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Accessors(chain = true)
@Setter
public class TestUserFactory {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String email = "user@example.ocm";
    private UserRole role = UserRole.USER;

    public User create() {
        return create(user -> {
        });
    }

    public User create(Consumer<User> consumer) {
        User user = User.builder().email(new Email(email)).displayName("username").passwordHash(passwordEncoder.encode("123123123")).role(role).build();
        System.out.println(role);
        consumer.accept(user);
        return userRepository.save(user);
    }
}
