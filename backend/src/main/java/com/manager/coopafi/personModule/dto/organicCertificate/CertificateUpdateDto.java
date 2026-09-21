package com.manager.coopafi.personModule.dto.organicCertificate;

import java.time.LocalDate;

public record CertificateUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}
