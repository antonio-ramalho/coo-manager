package com.manager.coomanager.personModule.dto.organicCertificate;

import java.time.LocalDate;

public record CertificateUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}
