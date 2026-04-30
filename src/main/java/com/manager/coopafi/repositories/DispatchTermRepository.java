package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.DispatchTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchTermRepository extends JpaRepository<DispatchTerm, Long> {
}
