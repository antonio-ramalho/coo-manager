package com.manager.coopafi.purchaseModule.dto.inputPurchaseItem;

import com.manager.coopafi.purchaseModule.entities.InputPurchaseItem;

/**
 * DTO detalhado de um item vendido, incluindo código do lote e unidade de medida.
 */
public record InputPurchaseItemDto(
        String productName,
        String batchCode,
        String measureUnit,
        Double quantity,
        Double unitPrice,
        Double itemTotalValue
) {
    public InputPurchaseItemDto(InputPurchaseItem entity) {
        this(
                entity.getInputBatch() != null && entity.getInputBatch().getInputProduct() != null
                        ? entity.getInputBatch().getInputProduct().getProductName()
                        : "Produto não identificado",
                entity.getInputBatch() != null ? entity.getInputBatch().resolveBatchCode() : "",
                entity.getInputBatch() != null
                        && entity.getInputBatch().getInputProduct() != null
                        && entity.getInputBatch().getInputProduct().getMeasureUnit() != null
                        ? entity.getInputBatch().getInputProduct().getMeasureUnit().name()
                        : "",
                entity.getQuantity() != null ? entity.getQuantity().getAmount().doubleValue() : 0.0,
                entity.getAppliedPrice() != null ? entity.getAppliedPrice().getValue().doubleValue() : 0.0,
                entity.getTotalPrice() != null ? entity.getTotalPrice().getValue().doubleValue() : 0.0
        );
    }
}
