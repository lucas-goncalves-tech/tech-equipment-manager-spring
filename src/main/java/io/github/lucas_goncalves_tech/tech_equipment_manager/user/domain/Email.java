package io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain;

import io.github.lucas_goncalves_tech.tech_equipment_manager.exception.DomainInvalidException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Email {
    private String email;

    public Email(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$")) {
            throw new DomainInvalidException("Formato de e-mail inválido");
        }

        this.email = email.trim().toLowerCase();
    }
}
