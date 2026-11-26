package com.manager.coomanager.farmerModule.repositories;

import com.manager.coomanager.farmerModule.entities.OrganicCertificate;
import com.manager.coomanager.farmerModule.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganicCertificateRepository extends JpaRepository<OrganicCertificate, Long> {
    Optional<OrganicCertificate> findByDocumentStatusAndId(DocumentStatus status, Long id);
}