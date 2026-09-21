package com.manager.coopafi.personModule.dto.agriculturalProduct;

import com.manager.coopafi.produtcModule.entities.AgriculturalProduct;

public record AgriculturalProductMinDto(
        Long id,
        String productName
) {
    public AgriculturalProductMinDto(AgriculturalProduct entity) {
        this(entity.getId(), entity.getProductName());
    }
}
