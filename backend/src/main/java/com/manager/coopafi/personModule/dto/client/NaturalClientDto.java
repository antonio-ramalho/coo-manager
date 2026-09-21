package com.manager.coopafi.personModule.dto.client;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;
import com.manager.coopafi.personModule.entities.NaturalPerson;
import com.manager.coopafi.personModule.interfaces.IClientDto;

import java.time.LocalDate;

public record NaturalClientDto(
        Long id,
        String legalName,
        AddressDto address,
        String email,
        String phone,
        String status,
        String personType,
        String cpf,
        LocalDate birthDate,
        String gender
) implements IClientDto {
    public NaturalClientDto(NaturalPerson entity) {
        this(
                entity.getId(),
                entity.getLegalName(),
                new AddressDto(entity.getAddress()),
                entity.getEmail().getAddressEmail(),
                entity.getPhone().getPhoneNumber(),
                entity.getStatus().getValue(),
                entity.getPersonType().getDescription(),
                entity.getCpf().getCpfNumber(),
                entity.getBirthDate().getDate(),
                entity.getGender().getDescription()
        );
    }
}
