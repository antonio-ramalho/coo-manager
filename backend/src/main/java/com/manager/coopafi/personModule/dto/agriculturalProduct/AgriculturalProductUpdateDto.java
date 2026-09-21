package com.manager.coopafi.personModule.dto.agriculturalProduct;

public record AgriculturalProductUpdateDto(
        String productName,
        Double priceValue,
        String cultivationType,
        String productGroup
) {}
