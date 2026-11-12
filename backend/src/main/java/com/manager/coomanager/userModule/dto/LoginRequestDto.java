package com.manager.coomanager.userModule.dto;

/**
 * DTO para submissão de credenciais de login.
 */
public record LoginRequestDto(
        String email,
        String password
) {
}

