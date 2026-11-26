package com.manager.coomanager.cooperativeModule.dto;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;
import com.manager.coomanager.shared.domain.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO com dados detalhados da Cooperativa retornado para a camada de visualização.
 */
public record CooperativeResponseDto(
        Long id,
        UUID uuid,
        String legalName,
        String tradeName,
        String cnpj,
        String stateRegistration,
        String email,
        String phone,
        LocalDate foundationDate,
        LocalDateTime registrationDate,
        Status status,
        AddressDto address
) {
    public CooperativeResponseDto(Cooperative entity) {
        this(
                entity.getId(),
                entity.getUuid(),
                entity.getLegalName(),
                entity.getTradeName(),
                entity.getCnpj() != null ? entity.getCnpj().getCnpjNumber() : null,
                entity.getStateRegistration() != null ? entity.getStateRegistration().getRegistrationNumber() : null,
                entity.getEmail() != null ? entity.getEmail().getAddressEmail() : null,
                entity.getPhone() != null ? entity.getPhone().getPhoneNumber() : null,
                entity.getFoundationDate(),
                entity.getRegistrationDate(),
                entity.getStatus(),
                entity.getAddress() != null ? new AddressDto(entity.getAddress()) : null
        );
    }
}
