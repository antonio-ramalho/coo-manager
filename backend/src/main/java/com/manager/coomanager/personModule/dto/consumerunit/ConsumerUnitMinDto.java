package com.manager.coomanager.personModule.dto.consumerunit;

import com.manager.coomanager.personModule.entities.ConsumerUnit;

public record ConsumerUnitMinDto(
        Long id,
        String legalName,
        String cnpj,
        boolean isSubsidiaryCNPJ
) {
    public ConsumerUnitMinDto(ConsumerUnit entity) {
        this(
                entity.getId(),
                entity.getJuridicPerson().getLegalName(),
                entity.getJuridicPerson().getCnpj().getCnpjNumber(),
                entity.isSubsidiaryCNPJ()
        );
    }
}
