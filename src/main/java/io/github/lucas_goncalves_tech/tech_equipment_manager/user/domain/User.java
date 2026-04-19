package io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Embedded
    @AttributeOverride(name = "email", column = @Column(unique = true, nullable = false, length = 200))
    private Email email;

    @Setter
    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Setter
    @Column(nullable = false)
    private String displayName;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role = UserRole.USER;

    @Column(nullable = false)
    private Boolean isActive = true;

    private String deactivatedReason;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public User(Email email, String passwordHash, String displayName, UserRole role){
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.role = (role != null) ? role : UserRole.USER;
    }

    public String getEmailRaw(){
        return email.getEmail();
    }

    public void deactivate(String reason){
        this.isActive = false;
        this.deactivatedReason = reason;
    }

    public void activate(){
        this.isActive = true;
        this.deactivatedReason = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email.getEmail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
