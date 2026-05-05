package com.manager.coopafi.dto.institution;

import com.manager.coopafi.domain.entities.Institution;

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

