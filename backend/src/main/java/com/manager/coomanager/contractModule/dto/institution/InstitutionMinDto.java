package com.manager.coomanager.contractModule.dto.institution;

import com.manager.coomanager.contractModule.entities.Institution;

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

