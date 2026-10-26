package com.manager.coopafi.purchaseModule.dto.inputProduct;

import com.manager.coopafi.purchaseModule.entities.InputBatch;
import java.time.LocalDate;

/**
 * DTO resumido de lote para exibição no painel de detalhes do produto no catálogo.
 */
public record InputProductBatchSummaryDto(
        Long id,
        String batchCode,
        Double currentQuantity,
        Double originalQuantity,
        LocalDate entryDate,
        LocalDate expirationDate,
        String status,
        String statusDescription
) {
    public InputProductBatchSummaryDto(InputBatch entity) {
        this(
                entity.getId(),
                entity.getBatchCode() != null && !entity.getBatchCode().isBlank()
                        ? entity.getBatchCode()
                        : (entity.getUuid() != null ? "LOT-" + entity.getUuid().toString().substring(0, 8).toUpperCase() : "LOT-" + entity.getId()),
                entity.getCurrentQuantity() != null ? entity.getCurrentQuantity().getAmount().doubleValue() : 0.0,
                entity.getOriginalQuantity() != null ? entity.getOriginalQuantity().getAmount().doubleValue() : 0.0,
                entity.getEntryDate(),
                entity.getExpirationDate() != null ? entity.getExpirationDate().getValue() : null,
                entity.getProductStatus() != null ? entity.getProductStatus().name() : "",
                entity.getProductStatus() != null ? entity.getProductStatus().getDescription() : ""
        );
    }
}
