package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.Caf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CafRepository extends JpaRepository<Caf, Long> {
}