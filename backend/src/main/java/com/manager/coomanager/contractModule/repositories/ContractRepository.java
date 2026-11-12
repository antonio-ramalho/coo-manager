package com.manager.coomanager.contractModule.repositories;

import com.manager.coomanager.contractModule.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
