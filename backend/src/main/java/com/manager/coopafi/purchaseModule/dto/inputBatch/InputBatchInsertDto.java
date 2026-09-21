package com.manager.coopafi.purchaseModule.dto.inputBatch;

import java.time.LocalDate;

public record InputBatchInsertDto(
        Long inputProductId,
        Double quantityValue,
        LocalDate expirationDate
) {}
