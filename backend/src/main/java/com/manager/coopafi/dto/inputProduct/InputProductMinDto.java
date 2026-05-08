package com.manager.coopafi.dto.inputProduct;

import com.manager.coopafi.domain.entities.InputProduct;

public record InputProductMinDto(
        Long id,
        String productName,
        String productCode
) {
    public InputProductMinDto(InputProduct entity) {
        this(entity.getId(), entity.getProductName(), entity.getProductCode());
    }
}
