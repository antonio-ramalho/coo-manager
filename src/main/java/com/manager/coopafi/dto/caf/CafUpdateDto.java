package com.manager.coopafi.dto.caf;

import java.time.LocalDate;

public record CafUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}