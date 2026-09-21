package com.manager.coopafi.purchaseModule.dto.inputBatch;

import java.time.LocalDate;

public record InputBatchUpdateDto(
        LocalDate expirationDate
) {}
