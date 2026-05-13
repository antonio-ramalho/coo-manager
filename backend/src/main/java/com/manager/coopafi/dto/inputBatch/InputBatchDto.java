package com.manager.coopafi.dto.inputBatch;

import com.manager.coopafi.domain.entities.InputBatch;
import java.time.LocalDate;

public record InputBatchDto(
        Long id,
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
                entity.getInputProduct().getId(),
                entity.getInputProduct().getProductName(),
                entity.getOriginalQuantity().getAmount().doubleValue(),
                entity.getCurrentQuantity().getAmount().doubleValue(),
                entity.getEntryDate(),
                entity.getExpirationDate().getValue(),
                entity.getProductStatus().name()
        );
    }
}