package com.manager.coopafi.dto.organicCertificate;

import java.time.LocalDate;

public record CertificateInsertDto(
        String certificateNumber,
        String institutionName,
        LocalDate expirationDate
) {}