package com.manager.coopafi.personModule.dto.caf;

import java.time.LocalDate;

public record CafUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}