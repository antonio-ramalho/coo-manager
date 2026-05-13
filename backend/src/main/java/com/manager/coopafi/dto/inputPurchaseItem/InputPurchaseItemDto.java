package com.manager.coopafi.dto.inputPurchaseItem;

import com.manager.coopafi.domain.entities.InputPurchaseItem;

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
