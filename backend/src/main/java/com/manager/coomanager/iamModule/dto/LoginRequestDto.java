package com.manager.coomanager.iamModule.dto;

/**
 * DTO para submissão de credenciais de login.
 */
public record LoginRequestDto(
        String email,
        String password
) {
}

