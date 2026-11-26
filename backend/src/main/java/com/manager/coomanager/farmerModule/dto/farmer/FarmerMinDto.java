package com.manager.coomanager.farmerModule.dto.farmer;

import com.manager.coomanager.farmerModule.entities.Farmer;

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
