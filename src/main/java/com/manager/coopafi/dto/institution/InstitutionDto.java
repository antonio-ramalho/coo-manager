package com.manager.coopafi.dto.institution;

import com.manager.coopafi.domain.entities.Institution;

import java.time.LocalDate;

public record InstitutionDto(
        Long id,
        String legalName,
        String tradeName,
        String cnpjNumber,
        LocalDate birthDate,
        String phoneNumber,
        String addressEmail,
        String cepNumber,
        String street,
        String neighborhood,
        String city,
        String addressNumber,
        String institutionSphere
) {
    public InstitutionDto(Institution entity) {
        this(
                entity.getId(),
                entity.getJuridicPerson().getLegalName(),
                entity.getJuridicPerson().getTradeName(),
                entity.getJuridicPerson().getCnpj().getCnpjNumber(),
                entity.getJuridicPerson().getBirthDate().getDate(),
                entity.getJuridicPerson().getPhone().getPhoneNumber(),
                entity.getJuridicPerson().getEmail().getAddressEmail(),
                entity.getJuridicPerson().getAddress().getCep().getCepNumber(),
                entity.getJuridicPerson().getAddress().getStreet(),
                entity.getJuridicPerson().getAddress().getNeighborhood(),
                entity.getJuridicPerson().getAddress().getCity(),
                entity.getJuridicPerson().getAddress().getAddressNumber(),
                entity.getInstitutionSphere().name()
        );
    }
}

