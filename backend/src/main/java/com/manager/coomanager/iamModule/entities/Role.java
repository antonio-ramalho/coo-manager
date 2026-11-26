package com.manager.coomanager.iamModule.entities;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.iamModule.enums.Permission;
import org.hibernate.annotations.Filter;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_role")
@Filter(name = "tenantFilter", condition = "(cooperative_id = :cooperativeId OR cooperative_id IS NULL)")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private boolean systemRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id")
    private Cooperative cooperative;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();

    public Role(String name, String description, Cooperative cooperative, boolean systemRole) {
        this.name = Objects.requireNonNull(name, "O nome do cargo não pode ser nulo.");
        this.description = description;
        this.cooperative = cooperative;
        this.systemRole = systemRole;
    }

    public void updateName(String name) {
        this.name = Objects.requireNonNull(name, "O nome do cargo não pode ser nulo.");
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void addPermission(Permission permission) {
        if (permission != null) {
            this.permissions.add(permission);
        }
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
    }

    public boolean hasPermission(Permission permission) {
        return this.permissions.contains(permission);
    }
}
