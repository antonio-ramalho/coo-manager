package com.manager.coomanager.personModule.dto.caf;

import java.time.LocalDate;

public record CafUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}