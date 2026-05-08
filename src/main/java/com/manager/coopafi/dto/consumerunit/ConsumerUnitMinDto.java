package com.manager.coopafi.dto.consumerunit;

import com.manager.coopafi.domain.entities.ConsumerUnit;

public record ConsumerUnitMinDto(
        Long id,
        String legalName,
        String cnpj
) {
    public ConsumerUnitMinDto(ConsumerUnit entity) {
        this(
                entity.getId(),
                entity.getJuridicPerson().getLegalName(),
                entity.getJuridicPerson().getCnpj().getCnpjNumber()
        );
    }
}
