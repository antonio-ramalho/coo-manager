package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.personModule.entities.ConsumerUnit;
import com.manager.coomanager.personModule.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumerUnitRepository extends JpaRepository<ConsumerUnit, Long> {
    List<ConsumerUnit> findByStatus(Status status);
    Optional<ConsumerUnit> findByStatusAndId(Status status, Long id);
}
