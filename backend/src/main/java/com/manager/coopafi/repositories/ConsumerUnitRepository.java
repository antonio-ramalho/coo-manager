package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.ConsumerUnit;
import com.manager.coopafi.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumerUnitRepository extends JpaRepository<ConsumerUnit, Long> {
    List<ConsumerUnit> findByStatus(UserStatus status);
    Optional<ConsumerUnit> findByStatusAndId(UserStatus status, Long id);
}
