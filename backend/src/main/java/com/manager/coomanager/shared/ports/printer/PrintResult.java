package com.manager.coomanager.shared.ports.printer;

import java.time.Instant;

/**
 * Resultado do processamento de uma solicitação de impressão.
 */
public record PrintResult(
        boolean success,
        String jobId,
        String message,
        Instant printedAt
) {
    public static PrintResult success(String jobId, String message) {
        return new PrintResult(true, jobId, message, Instant.now());
    }

    public static PrintResult failure(String message) {
        return new PrintResult(false, null, message, Instant.now());
    }
}
