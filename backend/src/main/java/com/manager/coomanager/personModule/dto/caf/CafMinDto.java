package com.manager.coomanager.personModule.dto.caf;

import com.manager.coomanager.personModule.entities.Caf;

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
