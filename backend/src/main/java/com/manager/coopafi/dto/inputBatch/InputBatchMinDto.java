package com.manager.coopafi.dto.inputBatch;

import com.manager.coopafi.domain.entities.InputBatch;

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
