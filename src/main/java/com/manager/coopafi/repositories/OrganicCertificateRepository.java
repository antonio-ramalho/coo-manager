package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.OrganicCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganicCertificateRepository extends JpaRepository<OrganicCertificate, Long> {
}