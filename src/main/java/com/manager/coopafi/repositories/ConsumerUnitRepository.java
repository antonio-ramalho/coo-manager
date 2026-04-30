package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.ConsumerUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerUnitRepository extends JpaRepository<ConsumerUnit, Long> {
}
