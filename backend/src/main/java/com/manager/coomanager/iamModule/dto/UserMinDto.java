package com.manager.coomanager.iamModule.dto;

import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.entities.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record UserMinDto(
        Long id,
        String fullName,
        String email,
        Status status,
        boolean twoFactorEnabled,
        Set<String> roleNames,
        LocalDateTime createdAt) {
    public UserMinDto(User entity) {
        this(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail() != null ? entity.getEmail().getAddressEmail() : null,
                entity.getStatus(),
                entity.isTwoFactorEnabled(),
                entity.getRoles() != null ? entity.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                        : Set.of(),
                entity.getCreatedAt());
    }
}
