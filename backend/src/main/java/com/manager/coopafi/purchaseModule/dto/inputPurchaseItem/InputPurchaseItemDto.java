package com.manager.coopafi.purchaseModule.dto.inputPurchaseItem;

import com.manager.coopafi.purchaseModule.entities.InputPurchaseItem;

public record InputPurchaseItemDto(
        String productName,
        Double quantity,
        Double unitPrice,
        Double itemTotalValue
) {
    public InputPurchaseItemDto(InputPurchaseItem entity) {
        this(
                entity.getInputBatch().getInputProduct().getProductName(),
                entity.getQuantity().getAmount().doubleValue(),
                entity.getAppliedPrice().getValue().doubleValue(),
                entity.getTotalPrice().getValue().doubleValue()
        );
    }
}
