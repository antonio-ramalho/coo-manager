package com.manager.coomanager.logisticModule.repositories;

import com.manager.coomanager.logisticModule.domain.entities.DispatchTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchTermRepository extends JpaRepository<DispatchTerm, Long> {
}
