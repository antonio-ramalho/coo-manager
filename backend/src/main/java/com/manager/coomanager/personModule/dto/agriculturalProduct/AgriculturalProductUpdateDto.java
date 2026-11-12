package com.manager.coomanager.personModule.dto.agriculturalProduct;

public record AgriculturalProductUpdateDto(
        String productName,
        Double priceValue,
        String cultivationType,
        String productGroup
) {}
