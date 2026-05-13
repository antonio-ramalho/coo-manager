package com.manager.coopafi.dto.agriculturalProduct;

import com.manager.coopafi.domain.entities.AgriculturalProduct;

public record AgriculturalProductMinDto(
        Long id,
        String productName
) {
    public AgriculturalProductMinDto(AgriculturalProduct entity) {
        this(entity.getId(), entity.getProductName());
    }
}
