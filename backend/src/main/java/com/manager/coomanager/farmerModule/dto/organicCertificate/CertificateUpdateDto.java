package com.manager.coomanager.farmerModule.dto.organicCertificate;

import java.time.LocalDate;

public record CertificateUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}
