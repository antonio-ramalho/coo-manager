package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.InputBatch;
import com.manager.coopafi.domain.entities.InputProduct;
import com.manager.coopafi.enums.ProductCatalogStatus;
import com.manager.coopafi.enums.ProductInventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InputBatchRepository extends JpaRepository<InputBatch, Long> {
    List<InputBatch> findByProductStatus(ProductInventoryStatus status);
}
