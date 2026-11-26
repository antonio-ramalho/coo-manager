package com.manager.coomanager.contractModule.dto.consumerunit;

import com.manager.coomanager.contractModule.entities.ConsumerUnit;

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
