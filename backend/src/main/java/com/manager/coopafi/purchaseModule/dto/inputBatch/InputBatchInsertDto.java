package com.manager.coopafi.purchaseModule.dto.inputBatch;

import java.time.LocalDate;

public record InputBatchInsertDto(
        String batchCode,
        Long inputProductId,
        Double quantityValue,
        LocalDate expirationDate
) {
    public InputBatchInsertDto(Long inputProductId, Double quantityValue, LocalDate expirationDate) {
        this(null, inputProductId, quantityValue, expirationDate);
    }
}
