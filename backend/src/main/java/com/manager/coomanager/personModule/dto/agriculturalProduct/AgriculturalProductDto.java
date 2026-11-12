package com.manager.coomanager.personModule.dto.agriculturalProduct;

import com.manager.coomanager.produtcModule.entities.AgriculturalProduct;

public record AgriculturalProductDto(
        Long id,
        String productName,
        String measureUnit,
        String ncm,
        Double price,
        String cultivationType,
        String productGroup
) {
    public AgriculturalProductDto(AgriculturalProduct entity) {
        this(
                entity.getId(),
                entity.getProductName(),
                entity.getMeasureUnit().name(),
                entity.getNcm().getCode(),
                entity.getProductPrice().getValue().doubleValue(), 
                entity.getCultivationType().name(),
                entity.getProductGroup().name()
        );
    }
}
