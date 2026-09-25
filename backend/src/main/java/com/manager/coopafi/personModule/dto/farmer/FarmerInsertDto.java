package com.manager.coopafi.personModule.dto.farmer;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;

import java.time.LocalDate;

public record FarmerInsertDto(
        String name,
        String cpfNumber,
        LocalDate birthDate,
        String phoneNumber,
        String addressEmail,
        AddressDto address,
        Long cafId,
        Long certificateId,
        String gender
) {}
