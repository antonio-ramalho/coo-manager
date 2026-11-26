package com.manager.coomanager.productModule.dto;

public record AgriculturalProductInsertDto (
        String productName,
        String measureUnit,   
        String ncmValue,
        Double priceValue,
        String cultivationType,
        String productGroup
) {}
