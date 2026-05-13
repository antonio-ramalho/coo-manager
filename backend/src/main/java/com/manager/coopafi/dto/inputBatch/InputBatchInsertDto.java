package com.manager.coopafi.dto.inputBatch;

import java.time.LocalDate;

public record InputBatchInsertDto(
        Long inputProductId,
        Double quantityValue,
        LocalDate expirationDate
) {}
