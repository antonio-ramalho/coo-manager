package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.personModule.entities.OrganicCertificate;
import com.manager.coopafi.personModule.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganicCertificateRepository extends JpaRepository<OrganicCertificate, Long> {
    Optional<OrganicCertificate> findByDocumentStatusAndId(DocumentStatus status, Long id);
}