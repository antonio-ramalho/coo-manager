package com.manager.coomanager.contractModule.dto.institution;

import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;
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

