package com.manager.coopafi.dto.inputProduct;

import java.time.LocalDate;

public record InputProductUpdateDto(
        String productName,
        LocalDate expirationDate,
        Double priceValue
) {}
