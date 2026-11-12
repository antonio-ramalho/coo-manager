package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.personModule.entities.OrganicCertificate;
import com.manager.coomanager.personModule.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganicCertificateRepository extends JpaRepository<OrganicCertificate, Long> {
    Optional<OrganicCertificate> findByDocumentStatusAndId(DocumentStatus status, Long id);
}