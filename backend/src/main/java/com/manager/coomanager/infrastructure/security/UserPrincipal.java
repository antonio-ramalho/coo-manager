package com.manager.coomanager.infrastructure.security;

import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.userModule.entities.Role;
import com.manager.coomanager.userModule.entities.User;
import com.manager.coomanager.userModule.enums.Permission;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Representação do usuário autenticado para o Spring Security.
 * Encapsula identificadores de Tenant (cooperativa), cargos e permissões.
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String password;
    private final Long cooperativeId;
    private final String cooperativeName;
    private final Status status;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String fullName, String email, String password,
                         Long cooperativeId, String cooperativeName, Status status,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.cooperativeId = cooperativeId;
        this.cooperativeName = cooperativeName;
        this.status = status;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase().replace(" ", "_")));
                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        authorities.add(new SimpleGrantedAuthority(permission.name()));
                    }
                }
            }
        }

        String password = user.getPasswordHash() != null ? user.getPasswordHash().getHash() : "";
        Long coopId = user.getCooperative() != null ? user.getCooperative().getId() : null;
        String coopName = user.getCooperative() != null ? user.getCooperative().getTradeName() : null;

        return new UserPrincipal(
                user.getId(),
                user.getFullName(),
                user.getEmail().getAddressEmail(),
                password,
                coopId,
                coopName,
                user.getStatus(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != Status.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE;
    }
}

