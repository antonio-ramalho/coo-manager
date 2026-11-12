package com.manager.coomanager.userModule.dto;

import com.manager.coomanager.userModule.enums.Permission;

import java.util.Set;

public record RoleRequestDto(
                String name,
                String description,
                Long cooperativeId,
                Set<Permission> permissions) {
}
