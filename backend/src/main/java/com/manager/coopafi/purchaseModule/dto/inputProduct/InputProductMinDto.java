package com.manager.coopafi.purchaseModule.dto.inputProduct;

import com.manager.coopafi.produtcModule.entities.InputProduct;

public record InputProductMinDto(
        Long id,
        String productName,
        String productCode
) {
    public InputProductMinDto(InputProduct entity) {
        this(entity.getId(), entity.getProductName(), entity.getProductCode());
    }
}
