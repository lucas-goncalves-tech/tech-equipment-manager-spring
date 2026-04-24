package io.github.lucas_goncalves_tech.tech_equipment_manager.auth;

import io.github.lucas_goncalves_tech.tech_equipment_manager.auth.dto.LoginRequest;
import io.github.lucas_goncalves_tech.tech_equipment_manager.core.security.JwtService;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(LoginRequest data) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.email(), data.password())
        );

        User user = (User) authentication.getPrincipal();

        return new TokenResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user)
        );
    }

    public record TokenResponse(String accessToken, String refreshToken) {
    }

}
