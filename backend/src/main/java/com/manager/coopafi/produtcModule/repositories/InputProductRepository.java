package com.manager.coopafi.produtcModule.repositories;

import com.manager.coopafi.produtcModule.entities.InputProduct;
import com.manager.coopafi.produtcModule.enums.ProductCatalogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InputProductRepository extends JpaRepository<InputProduct, Long> {
    List<InputProduct> findByProductCatalogStatus(ProductCatalogStatus status);
    Optional<InputProduct> findByProductCatalogStatusAndId(ProductCatalogStatus status, Long id);
    Optional<InputProduct> findByProductCode(String productCode);
}