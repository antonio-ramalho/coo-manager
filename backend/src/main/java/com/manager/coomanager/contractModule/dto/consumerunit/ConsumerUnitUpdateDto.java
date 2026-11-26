package com.manager.coomanager.contractModule.dto.consumerunit;

import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;

public record ConsumerUnitUpdateDto(
        String legalName,
        String tradeName,
        String addressEmail,
        String phoneNumber,
        boolean isSubsidiaryCNPJ,
        AddressDto address,
        AddressDto deliveryAddress
) {}


