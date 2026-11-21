package com.manager.coomanager.iamModule.dto;

import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.enums.Permission;

import java.util.Set;

public record RoleResponseDto(
        Long id,
        String name,
        String description,
        boolean systemRole,
        Long cooperativeId,
        Set<Permission> permissions) {
    public RoleResponseDto(Role role) {
        this(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.isSystemRole(),
                role.getCooperative() != null ? role.getCooperative().getId() : null,
                role.getPermissions());
    }
}
