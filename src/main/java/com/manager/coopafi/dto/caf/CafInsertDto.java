package com.manager.coopafi.dto.caf;

import java.time.LocalDate;

public record CafInsertDto(
        String cafNumber,
        String settlementType,
        LocalDate expirationDate
) {}
