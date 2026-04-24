package io.github.lucas_goncalves_tech.tech_equipment_manager.auth;

import io.github.lucas_goncalves_tech.tech_equipment_manager.auth.dto.LoginRequest;
import io.github.lucas_goncalves_tech.tech_equipment_manager.auth.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${jwt.refresh-expires-in-days}")
    private Long refreshExpiresInDays;

    private ResponseCookie setRefreshCookie(String token) {
        return setRefreshCookie(token, Duration.ofDays(refreshExpiresInDays));
    }

    private ResponseCookie setRefreshCookie(String token, Duration expiresIn) {
        boolean isProd = activeProfile.equals("prod");
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(isProd)
                .path("/")
                .sameSite("lax")
                .maxAge(expiresIn)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var tokens = authService.login(request);
        ResponseCookie cookie = setRefreshCookie(tokens.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(tokens.accessToken(), "Login realizado com sucesso"));
    }
}
