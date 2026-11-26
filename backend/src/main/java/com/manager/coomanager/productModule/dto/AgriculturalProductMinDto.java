package com.manager.coomanager.productModule.dto;

import com.manager.coomanager.productModule.entities.AgriculturalProduct;

public record AgriculturalProductMinDto(
        Long id,
        String productName
) {
    public AgriculturalProductMinDto(AgriculturalProduct entity) {
        this(entity.getId(), entity.getProductName());
    }
}
