package com.manager.coomanager.contractModule.dto.institution;

import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;

public record InstitutionUpdateDto(
        String tradeName,
        String legalName,
        String phoneNumber,
        String addressEmail,
        AddressDto address
) {}

