package com.manager.coopafi.dto.farmer;

public record FarmerUpdateDto(
        String phoneNumber,
        String addressEmail,
        String cepNumber,
        String street,
        String neighborhood,
        String city,
        String addressNumber,
        Long cafId,
        Long certificateId
) {}