package com.manager.coomanager.iamModule.dto;

/**
 * DTO para envio do código TOTP e token parcial (preAuthToken) na segunda etapa de login.
 */
public record TotpVerifyRequestDto(
        String preAuthToken,
        String code
) {
}

