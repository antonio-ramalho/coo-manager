package com.manager.coomanager.purchaseModule.events;

import java.time.LocalDate;
import java.util.List;

/**
 * Evento de domínio publicado quando lotes vencidos têm seu status atualizado automaticamente.
 */
public record BatchExpiredEvent(
        LocalDate referenceDate,
        List<Long> expiredBatchIds) {
}

