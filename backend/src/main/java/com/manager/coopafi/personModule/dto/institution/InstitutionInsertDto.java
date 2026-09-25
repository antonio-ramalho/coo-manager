package com.manager.coopafi.personModule.dto.institution;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;
import java.time.LocalDate;

public record InstitutionInsertDto(
        String cnpjNumber,
        String legalName,
        String tradeName,
        LocalDate birthDate,
        String phoneNumber,
        String addressEmail,
        AddressDto address,
        String institutionSphere
) {}

