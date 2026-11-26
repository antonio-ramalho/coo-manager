package com.manager.coomanager.iamModule.dto;

import com.manager.coomanager.shared.domain.enums.Status;
import com.manager.coomanager.iamModule.entities.User;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponseDto(
        Long id,
        UUID uuid,
        String fullName,
        String email,
        Long cooperativeId,
        String cooperativeName,
        Status status,
        boolean twoFactorEnabled,
        Set<RoleResponseDto> roles,
        LocalDateTime createdAt) {
    public UserResponseDto(User entity) {
        this(
                entity.getId(),
                entity.getUuid(),
                entity.getFullName(),
                entity.getEmail() != null ? entity.getEmail().getAddressEmail() : null,
                entity.getCooperative() != null ? entity.getCooperative().getId() : null,
                entity.getCooperative() != null ? entity.getCooperative().getTradeName() : null,
                entity.getStatus(),
                entity.isTwoFactorEnabled(),
                entity.getRoles() != null
                        ? entity.getRoles().stream().map(RoleResponseDto::new).collect(Collectors.toSet())
                        : Set.of(),
                entity.getCreatedAt());
    }
}
