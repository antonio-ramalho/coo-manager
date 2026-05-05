package com.manager.coopafi.dto;

import com.manager.coopafi.domain.entities.Farmer;

import java.time.LocalDate;

public record FarmerDto(
        Long id,
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
    public FarmerDto(Farmer farmer) {
        this(
                farmer.getId(),
                farmer.getPerson().getName(),
                farmer.getPerson().getCpf().getCpfNumber(),
                farmer.getPerson().getBirthDate().getDate(),
                farmer.getPerson().getPhone().getPhoneNumber(),
                farmer.getPerson().getEmail().getAddressEmail(),
                farmer.getPerson().getAddress().getAddressNumber(),
                farmer.getPerson().getAddress().getCity(),
                farmer.getPerson().getAddress().getNeighborhood(),
                farmer.getPerson().getAddress().getStreet(),
                farmer.getPerson().getAddress().getCep().getCepNumber()
        );
    }
}
