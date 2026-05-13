package com.manager.coopafi.dto.inputProduct;

import com.manager.coopafi.domain.entities.InputProduct;
import com.manager.coopafi.domain.valueObjects.Price;

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
