package com.manager.coomanager.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TotpServiceTest {

    private TotpService totpService;

    @BeforeEach
    void setUp() {
        totpService = new TotpService();
    }

    @Test
    @DisplayName("Deve gerar segredo em Base32 válido")
    void shouldGenerateValidBase32Secret() {
        String secret = totpService.generateSecret();

        assertThat(secret).isNotBlank();
        assertThat(secret).matches("^[A-Z2-7]+$");
        assertThat(secret.length()).isGreaterThanOrEqualTo(16);
    }

    @Test
    @DisplayName("Deve gerar URI do QR Code no padrão otpauth")
    void shouldGenerateCorrectQrCodeUri() {
        String secret = "JBSWY3DPEHPK3PXP";
        String email = "funcionario@coop.com";

        String uri = totpService.getQrCodeUri(secret, email);

        assertThat(uri).startsWith("otpauth://totp/CooManager:funcionario%40coop.com");
        assertThat(uri).contains("secret=" + secret);
        assertThat(uri).contains("issuer=CooManager");
        assertThat(uri).contains("digits=6");
    }

    @Test
    @DisplayName("Deve rejeitar códigos com tamanho inválido ou nulos")
    void shouldRejectInvalidFormatCodes() {
        String secret = "JBSWY3DPEHPK3PXP";

        assertThat(totpService.verifyCode(secret, null)).isFalse();
        assertThat(totpService.verifyCode(secret, "123")).isFalse();
        assertThat(totpService.verifyCode(secret, "abcdef")).isFalse();
        assertThat(totpService.verifyCode(null, "123456")).isFalse();
    }
}

