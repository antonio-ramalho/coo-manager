package com.manager.coomanager.personModule.dto.institution;

import com.manager.coomanager.infrastructure.valueObjectsDto.AddressDto;

public record InstitutionUpdateDto(
        String tradeName,
        String legalName,
        String phoneNumber,
        String addressEmail,
        AddressDto address
) {}

