package io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN(RoleName.ADMIN),
    USER(RoleName.USER);

    private final String authority;

    UserRole(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static class RoleName {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}
