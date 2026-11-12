package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.personModule.entities.Caf;
import com.manager.coomanager.personModule.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CafRepository extends JpaRepository<Caf, Long> {
    Optional<Caf> findByDocumentStatusAndId(DocumentStatus documentStatus, Long id);
}