package com.manager.coopafi.dto.inputBatch;

import java.time.LocalDate;

public record InputBatchUpdateDto(
        LocalDate expirationDate
) {}
