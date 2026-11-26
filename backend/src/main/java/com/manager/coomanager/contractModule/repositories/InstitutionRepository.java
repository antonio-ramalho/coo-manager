package com.manager.coomanager.contractModule.repositories;

import com.manager.coomanager.contractModule.entities.Institution;
import com.manager.coomanager.shared.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    List<Institution> findByStatus(Status status);

    Optional<Institution> findByStatusAndId(Status status, Long id);
}
