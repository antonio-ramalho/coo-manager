package com.manager.coopafi.personModule.dto.institution;

import com.manager.coopafi.personModule.entities.Institution;

public record InstitutionMinDto(
        Long id,
        String legalName,
        String cnpjNumber
) {
    public InstitutionMinDto(Institution entity) {
        this(
                entity.getId(),
                entity.getJuridicPerson().getLegalName(),
                entity.getJuridicPerson().getCnpj().getCnpjNumber()
        );
    }
}

