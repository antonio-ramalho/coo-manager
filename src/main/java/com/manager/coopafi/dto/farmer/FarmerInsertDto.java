package com.manager.coopafi.dto.farmer;

import java.time.LocalDate;

public record FarmerInsertDto(
        String name,
        String cpfNumber,
        LocalDate birthDate,
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
