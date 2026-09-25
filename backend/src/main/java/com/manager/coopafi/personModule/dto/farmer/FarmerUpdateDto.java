package com.manager.coopafi.personModule.dto.farmer;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;

public record FarmerUpdateDto(
        String phoneNumber,
        String addressEmail,
        AddressDto address,
        Long cafId,
        Long certificateId,
        String gender
) {}