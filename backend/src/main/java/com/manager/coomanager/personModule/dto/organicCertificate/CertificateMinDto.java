package com.manager.coomanager.personModule.dto.organicCertificate;

import com.manager.coomanager.personModule.entities.OrganicCertificate;

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
