package com.manager.coopafi.purchaseModule.repositories;

import com.manager.coopafi.purchaseModule.entities.InputProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InputProductRepository extends JpaRepository<InputProduct, Long> {

    Optional<InputProduct> findByProductCode(String productCode);

    @Query("SELECT p FROM InputProduct p WHERE " +
            "(:searchTerm IS NULL OR " +
            "LOWER(p.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.ncm) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            ")")
    Page<InputProduct> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
