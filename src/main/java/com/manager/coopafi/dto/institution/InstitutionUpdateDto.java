package com.manager.coopafi.dto.institution;

public record InstitutionUpdateDto(
        String tradeName,
        String legalName,
        String phoneNumber,
        String addressEmail,
        String cepNumber,
        String street,
        String neighborhood,
        String city,
        String addressNumber
) {}

