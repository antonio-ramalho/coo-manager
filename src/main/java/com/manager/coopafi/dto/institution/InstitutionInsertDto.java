package com.manager.coopafi.dto.institution;

import java.time.LocalDate;

public record InstitutionInsertDto(
        String cnpjNumber,
        String legalName,
        String tradeName,
        LocalDate birthDate,
        String phoneNumber,
        String addressEmail,
        String cepNumber,
        String street,
        String neighborhood,
        String city,
        String addressNumber,
        String institutionSphere
) {}

