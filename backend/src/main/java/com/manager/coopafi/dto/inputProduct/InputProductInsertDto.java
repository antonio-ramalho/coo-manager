package com.manager.coopafi.dto.inputProduct;

import java.time.LocalDate;

public record InputProductInsertDto(
        String productName,
        String productCode,
        LocalDate expirationDate,
        String measureUnit,
        String ncmValue,
        Double priceValue
) {}
