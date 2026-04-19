package io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class UserRoleTest {

    @Test
    @DisplayName("Should return correctly authority with prefix ROLE_")
    public void shouldReturnCorrectlyAuthorityWithPrefileROLE(){
        assertEquals("ROLE_ADMIN", UserRole.ADMIN.getAuthority());
        assertEquals("ROLE_USER", UserRole.USER.getAuthority());
    }
}
