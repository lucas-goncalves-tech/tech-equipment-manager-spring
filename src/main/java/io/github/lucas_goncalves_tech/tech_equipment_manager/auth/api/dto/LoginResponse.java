package io.github.lucas_goncalves_tech.tech_equipment_manager.auth.api.dto;

public record LoginResponse(
        String token,
        String message
) {
}
