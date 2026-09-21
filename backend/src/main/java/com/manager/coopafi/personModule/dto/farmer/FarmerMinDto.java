package com.manager.coopafi.personModule.dto.farmer;

import com.manager.coopafi.personModule.entities.Farmer;

public record FarmerMinDto(
        Long id,
        String name,
        String cpfNumber,
        String gender
) {
    public FarmerMinDto(Farmer entity) {
        this(
                entity.getId(),
                entity.getPerson().getLegalName(),
                entity.getPerson().getCpf().getCpfNumber(),
                entity.getPerson().getGender().getDescription()
        );
    }
}
