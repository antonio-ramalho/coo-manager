package com.manager.coopafi.purchaseModule.dto.dashboard;

import java.time.LocalDate;

public record ExpiringBatchDto(
                Long batchId,
                String productName,
                LocalDate expirationDate,
                long daysRemaining,
                Double currentQuantity) {
}
