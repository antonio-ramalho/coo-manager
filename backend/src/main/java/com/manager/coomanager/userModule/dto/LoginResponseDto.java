package com.manager.coomanager.userModule.dto;

/**
 * Resposta de login da aplicação.
 * Caso o segundo fator (2FA) esteja habilitado, retorna is2faRequired=true acompanhado do preAuthToken.
 * Caso contrário, retorna o accessToken final e os dados do usuário.
 */
public record LoginResponseDto(
        boolean is2faRequired,
        String preAuthToken,
        String accessToken,
        UserResponseDto user
) {
}

