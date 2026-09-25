package com.manager.coopafi.personModule.dto.consumerunit;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;

public record ConsumerUnitUpdateDto(
        String legalName,
        String tradeName,
        String addressEmail,
        String phoneNumber,
        boolean isSubsidiaryCNPJ,
        AddressDto address,
        AddressDto deliveryAddress
) {}


