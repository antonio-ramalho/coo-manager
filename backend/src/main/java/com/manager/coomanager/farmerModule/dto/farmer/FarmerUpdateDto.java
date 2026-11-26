package com.manager.coomanager.farmerModule.dto.farmer;

import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;

public record FarmerUpdateDto(
        String phoneNumber,
        String addressEmail,
        AddressDto address,
        Long cafId,
        Long certificateId,
        String gender
) {}