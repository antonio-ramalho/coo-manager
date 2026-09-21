package com.manager.coopafi.contractModule.repositories;

import com.manager.coopafi.contractModule.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
