package com.manager.coomanager.farmerModule.dto.caf;

import java.time.LocalDate;

public record CafInsertDto(
        String cafNumber,
        String settlementType,
        LocalDate expirationDate
) {}
