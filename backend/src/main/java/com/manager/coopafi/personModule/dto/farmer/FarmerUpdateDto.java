package com.manager.coopafi.personModule.dto.farmer;

public record FarmerUpdateDto(
        String phoneNumber,
        String addressEmail,
        String cepNumber,
        String street,
        String neighborhood,
        String city,
        String addressNumber,
        Long cafId,
        Long certificateId,
        String gender
) {}