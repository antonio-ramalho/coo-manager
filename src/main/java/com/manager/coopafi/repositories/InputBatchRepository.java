package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.InputBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputBatchRepository extends JpaRepository<InputBatch, Long> {
}
