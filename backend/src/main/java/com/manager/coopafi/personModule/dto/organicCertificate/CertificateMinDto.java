package com.manager.coopafi.personModule.dto.organicCertificate;

import com.manager.coopafi.personModule.entities.OrganicCertificate;

public record CertificateMinDto(
        Long id,
        String certificateNumber
) {
    public CertificateMinDto(OrganicCertificate entity) {
        this(
                entity.getId(),
                entity.getCertificateNumber()
        );
    }
}
