package com.manager.coomanager.userModule.dto;

/**
 * DTO para confirmação da ativação do 2FA informando o segredo temporário e o código gerado no app.
 */
public record TotpConfirmRequestDto(
        String secret,
        String code
) {
}

