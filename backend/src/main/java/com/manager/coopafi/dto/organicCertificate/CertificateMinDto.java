package com.manager.coopafi.dto.organicCertificate;

import com.manager.coopafi.domain.entities.OrganicCertificate;

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
