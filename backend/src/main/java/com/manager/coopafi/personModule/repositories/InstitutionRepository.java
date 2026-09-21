package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.personModule.entities.Institution;
import com.manager.coopafi.personModule.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    List<Institution> findByStatus(Status status);
    Optional<Institution> findByStatusAndId(Status status, Long id);
}
