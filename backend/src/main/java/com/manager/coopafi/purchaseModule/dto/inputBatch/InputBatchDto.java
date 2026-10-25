package com.manager.coopafi.purchaseModule.dto.inputBatch;

import com.manager.coopafi.purchaseModule.entities.InputBatch;
import java.time.LocalDate;

public record InputBatchDto(
        Long id,
        String batchCode,
        Long inputProductId,
        String productName,
        Double originalQuantity,
        Double currentQuantity,
        LocalDate entryDate,
        LocalDate expirationDate,
        String status
) {
    public InputBatchDto(InputBatch entity) {
        this(
                entity.getId(),
                entity.getBatchCode() != null ? entity.getBatchCode() : "",
                entity.getInputProduct() != null ? entity.getInputProduct().getId() : null,
                entity.getInputProduct() != null ? entity.getInputProduct().getProductName() : "",
                entity.getOriginalQuantity() != null ? entity.getOriginalQuantity().getAmount().doubleValue() : 0.0,
                entity.getCurrentQuantity() != null ? entity.getCurrentQuantity().getAmount().doubleValue() : 0.0,
                entity.getEntryDate(),
                entity.getExpirationDate() != null ? entity.getExpirationDate().getValue() : null,
                entity.getProductStatus() != null ? entity.getProductStatus().name() : ""
        );
    }
}