package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.InputProduct;
import com.manager.coopafi.enums.ProductCatalogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InputProductRepository extends JpaRepository<InputProduct, Long> {
    List<InputProduct> findByProductCatalogStatus(ProductCatalogStatus status);
    Optional<InputProduct> findByProductCatalogStatusAndId(ProductCatalogStatus status, Long id);
}