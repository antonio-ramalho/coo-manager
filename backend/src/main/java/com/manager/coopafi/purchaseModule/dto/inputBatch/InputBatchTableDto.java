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
                Double unitPrice,
                String measureUnit,
                String status,
                String statusDescription) {
        public InputBatchTableDto(InputBatch entity) {
                this(
                                entity.getId(),
                                entity.resolveBatchCode(),
                                entity.getInputProduct() != null ? entity.getInputProduct().getProductName() : "",
                                entity.getInputProduct() != null ? entity.getInputProduct().getProductCode() : "",
                                entity.getExpirationDate() != null ? entity.getExpirationDate().getValue() : null,
                                entity.getCurrentQuantity() != null
                                                ? entity.getCurrentQuantity().getAmount().doubleValue()
                                                : 0.0,
                                entity.getInputProduct() != null && entity.getInputProduct().getProductPrice() != null
                                                ? entity.getInputProduct().getProductPrice().getValue().doubleValue()
                                                : 0.0,
                                entity.getInputProduct() != null && entity.getInputProduct().getMeasureUnit() != null
                                                ? entity.getInputProduct().getMeasureUnit().name()
                                                : "",
                                entity.getProductStatus() != null ? entity.getProductStatus().name() : "",
                                entity.getProductStatus() != null ? entity.getProductStatus().getDescription() : "");
        }
}
