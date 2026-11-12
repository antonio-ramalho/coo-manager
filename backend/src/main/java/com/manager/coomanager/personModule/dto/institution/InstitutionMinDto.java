package com.manager.coomanager.personModule.dto.institution;

import com.manager.coomanager.personModule.entities.Institution;

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

