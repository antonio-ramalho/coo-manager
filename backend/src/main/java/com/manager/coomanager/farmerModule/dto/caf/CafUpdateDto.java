package com.manager.coomanager.farmerModule.dto.caf;

import java.time.LocalDate;

public record CafUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}