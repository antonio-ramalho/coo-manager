package com.manager.coopafi.purchaseModule.dto.inputBatch;

import com.manager.coopafi.purchaseModule.entities.InputBatch;
import java.time.LocalDate;

public record InputBatchTableDto(
        Long id,
        String batchCode,
        String productName,
        String productCode,
        LocalDate expirationDate,
        Double currentQuantity,
        String status,
        String statusDescription) {
    public InputBatchTableDto(InputBatch entity) {
        this(
                entity.getId(),
                entity.getUuid() != null ? "LOT-" + entity.getUuid().toString().substring(0, 8).toUpperCase()
                        : "LOT-" + entity.getId(),
                entity.getInputProduct() != null ? entity.getInputProduct().getProductName() : "",
                entity.getInputProduct() != null ? entity.getInputProduct().getProductCode() : "",
                entity.getExpirationDate() != null ? entity.getExpirationDate().getValue() : null,
                entity.getCurrentQuantity() != null ? entity.getCurrentQuantity().getAmount().doubleValue() : 0.0,
                entity.getProductStatus() != null ? entity.getProductStatus().name() : "",
                entity.getProductStatus() != null ? entity.getProductStatus().getDescription() : "");
    }
}
