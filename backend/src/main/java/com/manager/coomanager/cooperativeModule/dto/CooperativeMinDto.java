package com.manager.coomanager.cooperativeModule.dto;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.personModule.enums.Status;

import java.time.LocalDateTime;

/**
 * DTO resumido para listagem de Cooperativas.
 */
public record CooperativeMinDto(
        Long id,
        String legalName,
        String tradeName,
        String cnpj,
        String email,
        LocalDateTime registrationDate,
        Status status
) {
    public CooperativeMinDto(Cooperative entity) {
        this(
                entity.getId(),
                entity.getLegalName(),
                entity.getTradeName(),
                entity.getCnpj() != null ? entity.getCnpj().getCnpjNumber() : null,
                entity.getEmail() != null ? entity.getEmail().getAddressEmail() : null,
                entity.getRegistrationDate(),
                entity.getStatus()
        );
    }
}
