package com.manager.coopafi.purchaseModule.dto.dashboard;

import java.time.LocalDate;

/**
 * DTO que representa um lote vencido no painel de controle de estoque.
 */
public record ExpiredBatchDto(
        Long batchId,
        String productName,
        LocalDate expirationDate,
        long daysExpired,
        Double currentQuantity) {
}

