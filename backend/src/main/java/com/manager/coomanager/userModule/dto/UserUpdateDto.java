package com.manager.coomanager.userModule.dto;

import java.util.Set;

public record UserUpdateDto(
                String fullName,
                String email,
                Set<Long> roleIds) {
}
