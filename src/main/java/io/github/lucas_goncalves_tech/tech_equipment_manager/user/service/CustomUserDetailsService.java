package io.github.lucas_goncalves_tech.tech_equipment_manager.user.service;

import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.Email;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(username)).orElseThrow(() -> new UsernameNotFoundException("Email ou senha inválidos"));
    }
}
