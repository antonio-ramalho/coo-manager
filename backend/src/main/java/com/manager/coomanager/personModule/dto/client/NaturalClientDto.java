package com.manager.coomanager.personModule.dto.client;

import com.manager.coomanager.infrastructure.valueObjectsDto.AddressDto;
import com.manager.coomanager.personModule.entities.NaturalPerson;
import com.manager.coomanager.personModule.enums.Gender;
import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.personModule.interfaces.IClientDto;
import java.time.LocalDate;

public record NaturalClientDto(
        Long id,
        String legalName,
        AddressDto address,
        String email,
        String phone,
        Status status,
        String cpf,
        LocalDate birthDate,
        Gender gender
) implements IClientDto {
    public NaturalClientDto(NaturalPerson entity) {
        this(
                entity.getId(),
                entity.getLegalName(),
                new AddressDto(entity.getAddress()),
                entity.getEmail().getAddressEmail(),
                entity.getPhone().getPhoneNumber(),
                entity.getStatus(),
                entity.getCpf().getCpfNumber(),
                entity.getBirthDate().getDate(),
                entity.getGender()
        );
    }
}
