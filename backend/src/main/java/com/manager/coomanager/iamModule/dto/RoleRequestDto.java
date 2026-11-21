package com.manager.coomanager.iamModule.dto;

import com.manager.coomanager.iamModule.enums.Permission;

import java.util.Set;

public record RoleRequestDto(
                String name,
                String description,
                Long cooperativeId,
                Set<Permission> permissions) {
}
