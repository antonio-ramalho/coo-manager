package com.manager.coopafi.dto.agriculturalProduct;

public record AgriculturalProductUpdateDto(
        String productName,
        Double priceValue,
        String cultivationType,
        String productGroup
) {}
