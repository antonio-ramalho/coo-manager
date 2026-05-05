package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.Caf;
import com.manager.coopafi.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CafRepository extends JpaRepository<Caf, Long> {
    Optional<Caf> findByDocumentStatusAndId(DocumentStatus documentStatus, Long id);
}