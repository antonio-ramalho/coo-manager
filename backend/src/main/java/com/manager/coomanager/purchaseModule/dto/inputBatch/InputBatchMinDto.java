package com.manager.coomanager.purchaseModule.dto.inputBatch;

import com.manager.coomanager.purchaseModule.entities.InputBatch;

public record InputBatchMinDto(
        Long id,
        String productName,
        Double currentQuantity,
        String status
) {
    public InputBatchMinDto(InputBatch entity) {
        this(
                entity.getId(),
                entity.getInputProduct().getProductName(),
                entity.getCurrentQuantity().getAmount().doubleValue(),
                entity.getProductStatus().name()
        );
    }
}
