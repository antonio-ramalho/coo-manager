package com.manager.coopafi.personModule.dto.consumerunit;

import com.manager.coopafi.personModule.entities.ConsumerUnit;

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
