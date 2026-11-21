package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.productModule.entities.AgriculturalProduct;
import com.manager.coomanager.productModule.enums.ProductCatalogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AgriculturalProductRepository extends JpaRepository<AgriculturalProduct, Long> {
    List<AgriculturalProduct> findByProductCatalogStatus(ProductCatalogStatus status);

    Optional<AgriculturalProduct> findByProductCatalogStatusAndId(ProductCatalogStatus status, Long id);
}
