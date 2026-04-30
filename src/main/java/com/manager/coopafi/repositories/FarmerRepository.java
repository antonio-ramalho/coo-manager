package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.valueObjects.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByPerson_Cpf(Cpf cpf);
}