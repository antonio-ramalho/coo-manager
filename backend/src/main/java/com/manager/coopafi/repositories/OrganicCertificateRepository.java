package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.OrganicCertificate;
import com.manager.coopafi.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganicCertificateRepository extends JpaRepository<OrganicCertificate, Long> {
    Optional<OrganicCertificate> findByDocumentStatusAndId(DocumentStatus status, Long id);
}