package com.manager.coopafi.personModule.dto.caf;

import com.manager.coopafi.personModule.entities.Caf;

import java.time.LocalDate;

public record CafMinDto(
        Long id,
        String cafNumber,
        LocalDate expirationDate
) {
    public CafMinDto(Caf entity) {
        this(
                entity.getId(),
                entity.getCafNumber().getValue(),
                entity.getExpirationDate().getValue()
        );
    }
}
