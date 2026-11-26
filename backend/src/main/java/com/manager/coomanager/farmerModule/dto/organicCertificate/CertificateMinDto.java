package com.manager.coomanager.farmerModule.dto.organicCertificate;

import com.manager.coomanager.farmerModule.entities.OrganicCertificate;

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
