package com.manager.coopafi.personModule.dto.agriculturalProduct;

public record AgriculturalProductInsertDto (
        String productName,
        String measureUnit,   
        String ncmValue,
        Double priceValue,
        String cultivationType,
        String productGroup
) {}
