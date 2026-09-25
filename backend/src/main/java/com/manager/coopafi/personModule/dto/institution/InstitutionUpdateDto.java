package com.manager.coopafi.personModule.dto.institution;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;

public record InstitutionUpdateDto(
        String tradeName,
        String legalName,
        String phoneNumber,
        String addressEmail,
        AddressDto address
) {}

