package com.manager.coopafi.purchaseModule.repositories;

import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InputBatchRepository extends JpaRepository<InputBatch, Long> {
    List<InputBatch> findByProductStatus(ProductInventoryStatus status);
}
