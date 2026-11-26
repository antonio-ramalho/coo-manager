package com.manager.coomanager.farmerModule.repositories;

import com.manager.coomanager.farmerModule.entities.Farmer;
import com.manager.coomanager.shared.domain.valueobjects.Cpf;
import com.manager.coomanager.shared.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByPerson_Cpf(Cpf cpf);

    List<Farmer> findByStatus(Status status);

    Optional<Farmer> findByStatusAndId(Status status, Long id);
}