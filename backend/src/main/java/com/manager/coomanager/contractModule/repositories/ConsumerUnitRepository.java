package com.manager.coomanager.contractModule.repositories;

import com.manager.coomanager.contractModule.entities.ConsumerUnit;
import com.manager.coomanager.shared.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConsumerUnitRepository extends JpaRepository<ConsumerUnit, Long> {
    List<ConsumerUnit> findByStatus(Status status);

    Optional<ConsumerUnit> findByStatusAndId(Status status, Long id);
}
