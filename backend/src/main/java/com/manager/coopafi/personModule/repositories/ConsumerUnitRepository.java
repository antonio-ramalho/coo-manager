package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.personModule.entities.ConsumerUnit;
import com.manager.coopafi.personModule.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumerUnitRepository extends JpaRepository<ConsumerUnit, Long> {
    List<ConsumerUnit> findByStatus(Status status);
    Optional<ConsumerUnit> findByStatusAndId(Status status, Long id);
}
