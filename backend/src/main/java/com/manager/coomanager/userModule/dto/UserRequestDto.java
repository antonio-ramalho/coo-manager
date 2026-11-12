package com.manager.coomanager.userModule.dto;

import java.util.Set;

public record UserRequestDto(
                String fullName,
                String email,
                String password,
                Long cooperativeId,
                Set<Long> roleIds) {
}
