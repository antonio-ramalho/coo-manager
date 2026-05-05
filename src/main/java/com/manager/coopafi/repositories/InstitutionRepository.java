package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.Institution;
import com.manager.coopafi.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    List<Institution> findByStatus(UserStatus status);
    Optional<Institution> findByStatusAndId(UserStatus status, Long id);
}
