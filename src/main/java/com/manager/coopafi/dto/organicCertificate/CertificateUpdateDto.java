package com.manager.coopafi.dto.organicCertificate;

import java.time.LocalDate;

public record CertificateUpdateDto(
        LocalDate expirationDate,
        String documentStatus
) {}
