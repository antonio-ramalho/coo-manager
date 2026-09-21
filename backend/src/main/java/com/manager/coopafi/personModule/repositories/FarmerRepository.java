package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.personModule.entities.Farmer;
import com.manager.coopafi.infrastructure.valueObjects.Cpf;
import com.manager.coopafi.personModule.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByPerson_Cpf(Cpf cpf);
    List<Farmer> findByStatus(Status status);
    Optional<Farmer> findByStatusAndId(Status status, Long id);
}