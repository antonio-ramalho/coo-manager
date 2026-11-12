package com.manager.coomanager.purchaseModule.dto.inputBatch;

import java.time.LocalDate;

public record InputBatchUpdateDto(
        LocalDate expirationDate
) {}
