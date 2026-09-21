package com.manager.coopafi.personModule.dto.organicCertificate;

import com.manager.coopafi.personModule.entities.Farmer;
import com.manager.coopafi.personModule.entities.OrganicCertificate;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record CertificateDto(
        Long id,
        String certificateNumber,
        String institutionName,
        LocalDate expirationDate,
        String documentStatus,
        List<Long> farmerIds
) {
    public CertificateDto(OrganicCertificate entity) {
        this(
                entity.getId(),
                entity.getCertificateNumber(),
                entity.getInstitutionName(),
                entity.getExpirationDate().getValue(),
                entity.getDocumentStatus().name(),
                entity.getFarmers().stream().map(Farmer::getId).collect(Collectors.toList())
        );
    }
}
