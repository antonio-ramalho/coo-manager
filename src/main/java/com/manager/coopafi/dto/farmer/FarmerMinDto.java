package com.manager.coopafi.dto.farmer;

import com.manager.coopafi.domain.entities.Farmer;

public record FarmerMinDto(
        Long id,
        String name,
        String cpfNumber
) {
    public FarmerMinDto(Farmer entity) {
        this(
                entity.getId(),
                entity.getPerson().getName(),
                entity.getPerson().getCpf().getCpfNumber()
        );
    }
}
