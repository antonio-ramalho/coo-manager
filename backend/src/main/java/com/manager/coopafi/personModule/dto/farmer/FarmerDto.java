package com.manager.coopafi.personModule.dto.farmer;

import com.manager.coopafi.personModule.entities.Farmer;
import com.manager.coopafi.personModule.dto.caf.CafMinDto;
import com.manager.coopafi.personModule.dto.organicCertificate.CertificateMinDto;
import java.time.LocalDate;

public record FarmerDto(
        Long id,
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
        CafMinDto caf,
        CertificateMinDto certificate,
        String gender
) {
    public FarmerDto(Farmer entity) {
        this(
                entity.getId(),
                entity.getPerson().getLegalName(),
                entity.getPerson().getCpf().getCpfNumber(),
                entity.getPerson().getBirthDate().getDate(),
                entity.getPerson().getPhone().getPhoneNumber(),
                entity.getPerson().getEmail().getAddressEmail(),
                entity.getPerson().getAddress().getCep().getCepNumber(),
                entity.getPerson().getAddress().getStreet(),
                entity.getPerson().getAddress().getNeighborhood(),
                entity.getPerson().getAddress().getCity(),
                entity.getPerson().getAddress().getAddressNumber(),
                entity.getCaf() != null ? new CafMinDto(entity.getCaf()) : null,
                entity.getCertificate() != null ? new CertificateMinDto(entity.getCertificate()) : null,
                entity.getPerson().getGender().getDescription()
        );
    }
}
