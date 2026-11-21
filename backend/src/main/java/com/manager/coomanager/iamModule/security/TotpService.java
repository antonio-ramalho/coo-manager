package com.manager.coomanager.iamModule.security;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

/**
 * Serviço responsável pela geração e validação de códigos TOTP (Time-Based One-Time Password)
 * em total conformidade com a especificação RFC 6238 e RFC 4648 (Base32).
 * Compatível com Google Authenticator, Microsoft Authenticator e Authy.
 */
@Service
public class TotpService {

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final int TIME_STEP_SECONDS = 30;
    private static final int CODE_DIGITS = 6;
    private static final int WINDOW_SIZE = 1; // Permite drift de -30s até +30s
    private static final String ISSUER = "CooManager";
    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Gera um segredo aleatório de 160 bits (20 bytes) codificado em Base32.
     */
    public String generateSecret() {
        byte[] buffer = new byte[20];
        secureRandom.nextBytes(buffer);
        return encodeBase32(buffer);
    }

    /**
     * Gera a URI compatível com leitores de QR Code para aplicativos autenticadores.
     */
    public String getQrCodeUri(String secret, String accountName) {
        String encodedIssuer = URLEncoder.encode(ISSUER, StandardCharsets.UTF_8);
        String encodedAccount = URLEncoder.encode(accountName, StandardCharsets.UTF_8);
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=%d&period=%d",
                encodedIssuer, encodedAccount, secret, encodedIssuer, CODE_DIGITS, TIME_STEP_SECONDS);
    }

    /**
     * Valida o código numérico de 6 dígitos submetido pelo usuário.
     * Considera a janela de tempo de tolerância (atual, anterior e próxima).
     */
    public boolean verifyCode(String secret, String code) {
        if (secret == null || code == null || code.trim().length() != CODE_DIGITS) {
            return false;
        }

        try {
            int numericCode = Integer.parseInt(code.trim());
            byte[] key = decodeBase32(secret);
            long currentWindow = System.currentTimeMillis() / 1000L / TIME_STEP_SECONDS;

            for (int i = -WINDOW_SIZE; i <= WINDOW_SIZE; i++) {
                long window = currentWindow + i;
                int calculatedCode = generateTotpCode(key, window);
                if (calculatedCode == numericCode) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    private int generateTotpCode(byte[] key, long timeWindow) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = ByteBuffer.allocate(8).putLong(timeWindow).array();

        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
        byte[] hash = mac.doFinal(data);

        int offset = hash[hash.length - 1] & 0x0F;
        int binary = ((hash[offset] & 0x7F) << 24) |
                ((hash[offset + 1] & 0xFF) << 16) |
                ((hash[offset + 2] & 0xFF) << 8) |
                (hash[offset + 3] & 0xFF);

        int divisor = (int) Math.pow(10, CODE_DIGITS);
        return binary % divisor;
    }

    private String encodeBase32(byte[] data) {
        StringBuilder result = new StringBuilder();
        int buffer = 0;
        int bitsLeft = 0;

        for (byte b : data) {
            buffer = (buffer << 8) | (b & 0xFF);
            bitsLeft += 8;
            while (bitsLeft >= 5) {
                bitsLeft -= 5;
                int index = (buffer >> bitsLeft) & 0x1F;
                result.append(BASE32_ALPHABET.charAt(index));
            }
        }

        if (bitsLeft > 0) {
            buffer = buffer << (5 - bitsLeft);
            int index = buffer & 0x1F;
            result.append(BASE32_ALPHABET.charAt(index));
        }

        return result.toString();
    }

    private byte[] decodeBase32(String base32) {
        String clean = base32.toUpperCase(Locale.ROOT).replaceAll("[^A-Z2-7]", "");
        ByteBuffer byteBuffer = ByteBuffer.allocate(clean.length() * 5 / 8 + 1);

        int buffer = 0;
        int bitsLeft = 0;

        for (int i = 0; i < clean.length(); i++) {
            char c = clean.charAt(i);
            int val = BASE32_ALPHABET.indexOf(c);
            if (val < 0) {
                continue;
            }

            buffer = (buffer << 5) | val;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                bitsLeft -= 8;
                byteBuffer.put((byte) ((buffer >> bitsLeft) & 0xFF));
            }
        }

        byteBuffer.flip();
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }
}

