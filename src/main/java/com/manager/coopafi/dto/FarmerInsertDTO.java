package com.manager.coopafi.dto;

import java.time.LocalDate;

public record FarmerInsertDTO(
       String name,
       String cpfNumber,
       LocalDate date,
       String phoneNumber,
       String addressEmail,
       String addressNumber,
       String city,
       String neighborhood,
       String street,
       String cepNumber
) {
}