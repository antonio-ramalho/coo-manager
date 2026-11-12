package com.manager.coomanager.cooperativeModule.repositories;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.infrastructure.valueObjects.Cnpj;
import com.manager.coomanager.infrastructure.valueObjects.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CooperativeRepository extends JpaRepository<Cooperative, Long> {

    boolean existsByCnpj(Cnpj cnpj);

    boolean existsByEmail(Email email);

    Optional<Cooperative> findByCnpj(Cnpj cnpj);

    Optional<Cooperative> findByEmail(Email email);

    @Query("SELECT c FROM Cooperative c WHERE " +
            "(:searchTerm IS NULL OR :searchTerm = '' OR " +
            "LOWER(c.legalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.tradeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.email.addressEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "c.cnpj.cnpjNumber LIKE CONCAT('%', :searchTerm, '%'))")
    Page<Cooperative> findAllBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
