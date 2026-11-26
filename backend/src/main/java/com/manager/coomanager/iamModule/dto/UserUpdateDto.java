package com.manager.coomanager.iamModule.dto;

import java.util.Set;

public record UserUpdateDto(
                String fullName,
                String email,
                Set<Long> roleIds) {
}
