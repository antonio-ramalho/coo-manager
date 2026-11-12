package com.manager.coomanager.personModule.dto.institution;

import com.manager.coomanager.infrastructure.valueObjectsDto.AddressDto;
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

