package com.manager.coomanager.personModule.dto.farmer;

import com.manager.coomanager.infrastructure.valueObjectsDto.AddressDto;

public record FarmerUpdateDto(
        String phoneNumber,
        String addressEmail,
        AddressDto address,
        Long cafId,
        Long certificateId,
        String gender
) {}