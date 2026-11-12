package com.manager.coomanager.userModule.dto;

/**
 * Resposta para inicialização da configuração de 2FA (TOTP).
 */
public record TotpSetupResponseDto(
        String secret,
        String qrCodeUri
) {
}

