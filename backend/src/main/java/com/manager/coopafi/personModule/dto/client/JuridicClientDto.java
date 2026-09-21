package com.manager.coopafi.personModule.dto.client;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;
import com.manager.coopafi.personModule.entities.JuridicPerson;
import com.manager.coopafi.personModule.interfaces.IClientDto;
import java.time.LocalDate;

public record JuridicClientDto(
        Long id,
        String legalName,
        AddressDto address,
        String email,
        String phone,
        String status,
        String personType,
        String cnpj,
        LocalDate foundationDate,
        String tradeName
) implements IClientDto {
    public JuridicClientDto(JuridicPerson entity) {
        this(
                entity.getId(),
                entity.getLegalName(),
                new AddressDto(entity.getAddress()),
                entity.getEmail().getAddressEmail(),
                entity.getPhone().getPhoneNumber(),
                entity.getStatus().getValue(),
                entity.getPersonType().getDescription(),
                entity.getCnpj().getCnpjNumber(),
                entity.getFoundationDate(),
                entity.getTradeName()
        );
    }
}
