package com.manager.coomanager.personModule.dto.consumerunit;

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


