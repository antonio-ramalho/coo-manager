package com.manager.coomanager.iamModule.entities;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.shared.domain.valueobjects.Email;
import com.manager.coomanager.shared.domain.valueobjects.PasswordHash;
import com.manager.coomanager.shared.domain.enums.Status;
import com.manager.coomanager.iamModule.enums.Permission;
import org.hibernate.annotations.Filter;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_user")
@Filter(name = "tenantFilter", condition = "cooperative_id = :cooperativeId")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String fullName;

    @Embedded
    private Email email;

    @Embedded
    private PasswordHash passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Cooperative cooperative;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private boolean twoFactorEnabled;

    @Column(name = "two_factor_secret")
    private String twoFactorSecret;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User(String fullName, Email email, PasswordHash passwordHash, Cooperative cooperative) {
        this.fullName = Objects.requireNonNull(fullName, "O nome do usuário não pode ser nulo.");
        this.email = Objects.requireNonNull(email, "O e-mail não pode ser nulo.");
        this.passwordHash = passwordHash;
        this.cooperative = Objects.requireNonNull(cooperative, "A cooperativa não pode ser nula.");
        this.uuid = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.status = Status.ACTIVE;
        this.twoFactorEnabled = false;
    }

    public void updateFullName(String fullName) {
        this.fullName = Objects.requireNonNull(fullName, "O nome do usuário não pode ser nulo.");
    }

    public void updateEmail(Email email) {
        this.email = Objects.requireNonNull(email, "O e-mail não pode ser nulo.");
    }

    public void updatePassword(PasswordHash passwordHash) {
        this.passwordHash = Objects.requireNonNull(passwordHash, "A senha não pode ser nula.");
    }

    public void addRole(Role role) {
        if (role != null) {
            this.roles.add(role);
        }
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    public void enableTwoFactor(String secret) {
        this.twoFactorSecret = Objects.requireNonNull(secret, "O código não pode ser nulo.");
        this.twoFactorEnabled = true;
    }

    public void disableTwoFactor() {
        this.twoFactorEnabled = false;
        this.twoFactorSecret = null;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }

    public void deactivate() {
        this.status = Status.INACTIVE;
    }

    public void suspend() {
        this.status = Status.SUSPENDED;
    }

    public boolean hasPermission(Permission permission) {
        return this.roles.stream().anyMatch(role -> role.hasPermission(permission));
    }
}
