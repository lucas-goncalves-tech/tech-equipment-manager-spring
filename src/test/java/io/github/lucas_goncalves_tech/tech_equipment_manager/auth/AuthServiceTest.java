package io.github.lucas_goncalves_tech.tech_equipment_manager.auth;

import io.github.lucas_goncalves_tech.tech_equipment_manager.auth.dto.LoginRequest;
import io.github.lucas_goncalves_tech.tech_equipment_manager.core.security.JwtService;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.Email;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.User;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("Auth Unit Test")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("Method: login()")
    public class MethodLogin {

        @Test
        @DisplayName("Should return Token Response with access and refresh token when credentials are valid")
        public void shouldReturnTokenResponseWithAccessAndRefreshToken_WhenCredentialsAreValid() {
            final String email = "email@example.com";
            final String accessToken = "access-token";
            final String refreshToken = "refresh-token";
            final LoginRequest request = new LoginRequest(email, "raw-password");
            User user = User.builder().email(new Email(email)).displayName("username").passwordHash("hashed").role(UserRole.USER).build();
            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            when(authenticationManager.authenticate(any())).thenReturn(auth);
            when(jwtService.generateAccessToken(user)).thenReturn(accessToken);
            when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

            var response = authService.login(request);

            assertThat(response.accessToken()).isEqualTo(accessToken);
            assertThat(response.refreshToken()).isEqualTo(refreshToken);
        }
    }

}
