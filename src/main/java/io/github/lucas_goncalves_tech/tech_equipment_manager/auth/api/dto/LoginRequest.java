package io.github.lucas_goncalves_tech.tech_equipment_manager.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8, message = "Senha deve ter no minimo 8 caracteres")
        @Size(max = 32, message = "Senha deve ter no máximo 32 caracteres")
        String password
) {
}
