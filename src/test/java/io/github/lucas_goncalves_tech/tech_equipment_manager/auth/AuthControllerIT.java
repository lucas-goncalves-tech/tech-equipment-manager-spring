package io.github.lucas_goncalves_tech.tech_equipment_manager.auth;

import io.github.lucas_goncalves_tech.tech_equipment_manager.BaseIT;
import io.github.lucas_goncalves_tech.tech_equipment_manager.auth.dto.LoginRequest;
import io.github.lucas_goncalves_tech.tech_equipment_manager.support.TestUserFactory;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.User;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("Auth Integration Test")
public class AuthControllerIT extends BaseIT {

    @Autowired
    private TestUserFactory userFactory;

    @BeforeEach
    public void clean(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST: login()")
    public class PostLogin {

        private final String loginUrl = "/v1/auth/login";

        @Test
        @DisplayName("Should return 200 with access on body and refresh on cookie when credentials are valid")
        public void shouldReturn200WithAccessOnBodyAndRefreshOnCookie_WhenCredentialsAreValid() throws Exception {
            User user = userFactory.create();
            LoginRequest request = new LoginRequest(user.getEmailRaw(), "123123");
            String json = objectMapper.writeValueAsString(request);

            mockMvc.perform(post(loginUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().httpOnly("refreshToken", true))
                    .andExpect(jsonPath("$.token").isNotEmpty())
                    .andExpect(jsonPath("$.message").isNotEmpty());

        }
    }
}
