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
            LoginRequest request = new LoginRequest(user.getEmailRaw(), "123123123");
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

        @Test
        @DisplayName("Should return 400 with invalid_params when fields are invalid")
        public void shouldReturn400WithInvalidParams_WhenFieldsAreInvalid() throws Exception {
            LoginRequest request = new LoginRequest("non-valid", "");
            String json = objectMapper.writeValueAsString(request);

            mockMvc.perform(post(loginUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.title").exists())
                    .andExpect(jsonPath("$.detail").exists())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.type").value("urn:error:badRequest"))
                    .andExpect(jsonPath("$.invalid_params").isArray())
                    .andExpect(jsonPath("$.invalid_params[?(@.field == 'email')]").exists())
                    .andExpect(jsonPath("$.invalid_params[?(@.field == 'password')]").exists())
                    .andExpect(jsonPath("$.invalid_params[0].reason").exists());
        }

        @Test
        @DisplayName("Should return 401 when credentials are invalid")
        public void shouldReturn401_WhenCredentialsAreInvalid() throws Exception {
            LoginRequest request = new LoginRequest("non-exist@email.com", "123123123");
            String json = objectMapper.writeValueAsString(request);

            mockMvc.perform(post(loginUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.title").exists())
                    .andExpect(jsonPath("$.detail").exists())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.type").value("urn:error:unauthorized"));
        }
    }
}
