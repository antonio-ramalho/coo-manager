package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.produtcModule.entities.AgriculturalProduct;
import com.manager.coopafi.produtcModule.enums.ProductCatalogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgriculturalProductRepository extends JpaRepository<AgriculturalProduct, Long> {
    List<AgriculturalProduct> findByProductCatalogStatus(ProductCatalogStatus status);
    Optional<AgriculturalProduct> findByProductCatalogStatusAndId(ProductCatalogStatus status, Long id);
}
