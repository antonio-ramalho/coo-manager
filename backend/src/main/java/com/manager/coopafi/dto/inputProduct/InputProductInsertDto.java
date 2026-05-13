package com.manager.coopafi.dto.inputProduct;

public record InputProductInsertDto(
        String productName,
        String productCode,
        String measureUnit,
        String ncmValue,
        Double priceValue
) {}
