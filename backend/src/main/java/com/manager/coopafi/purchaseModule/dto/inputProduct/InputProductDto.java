package com.manager.coopafi.purchaseModule.dto.inputProduct;

import com.manager.coopafi.produtcModule.entities.InputProduct;
import com.manager.coopafi.infrastructure.valueObjects.Price;

public record InputProductDto(
        Long id,
        String productName,
        String productCode,
        String measureUnit,
        String ncm,
        Price price
) {
    public InputProductDto(InputProduct entity) {
        this(
                entity.getId(),
                entity.getProductName(),
                entity.getProductCode(),
                entity.getMeasureUnit().name(),
                entity.getNcm().getCode(),
                entity.getProductPrice()
        );
    }
}
