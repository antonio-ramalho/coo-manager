package com.manager.coomanager.productModule.dto;

public record AgriculturalProductUpdateDto(
        String productName,
        Double priceValue,
        String cultivationType,
        String productGroup
) {}
