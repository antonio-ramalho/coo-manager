package com.manager.coomanager.personModule.dto.farmer;

import com.manager.coomanager.personModule.entities.Farmer;

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
