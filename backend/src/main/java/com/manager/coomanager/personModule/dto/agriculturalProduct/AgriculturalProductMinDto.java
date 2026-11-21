package com.manager.coomanager.personModule.dto.agriculturalProduct;

import com.manager.coomanager.productModule.entities.AgriculturalProduct;

public record AgriculturalProductMinDto(
        Long id,
        String productName
) {
    public AgriculturalProductMinDto(AgriculturalProduct entity) {
        this(entity.getId(), entity.getProductName());
    }
}
