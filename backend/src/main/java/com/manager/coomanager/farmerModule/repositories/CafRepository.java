package com.manager.coomanager.farmerModule.repositories;

import com.manager.coomanager.farmerModule.entities.Caf;
import com.manager.coomanager.farmerModule.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CafRepository extends JpaRepository<Caf, Long> {
    Optional<Caf> findByDocumentStatusAndId(DocumentStatus documentStatus, Long id);
}