package com.manager.coopafi.purchaseModule.repositories;

import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InputBatchRepository extends JpaRepository<InputBatch, Long> {

    List<InputBatch> findByProductStatus(ProductInventoryStatus status);

    @Query("""
                SELECT COALESCE(SUM(b.currentQuantity.amount * b.inputProduct.productPrice.value), 0)
                FROM InputBatch b
                WHERE b.productStatus IN (
                    com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus.IN_STOCK,
                    com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus.LOW_STOCK
                )
            """)
    BigDecimal calculateTotalStockValue();

    @Query("SELECT COUNT(b) FROM InputBatch b WHERE b.productStatus = :status")
    long countByProductStatus(@Param("status") ProductInventoryStatus status);

    @Query("""
                SELECT b FROM InputBatch b
                WHERE b.productStatus IN (
                    com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus.IN_STOCK,
                    com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus.LOW_STOCK
                )
                AND b.expirationDate.value BETWEEN :startDate AND :endDate
                ORDER BY b.expirationDate.value ASC
            """)
    List<InputBatch> findExpiringBatches(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT b FROM InputBatch b
                WHERE (:searchTerm IS NULL OR :searchTerm = '' OR
                       LOWER(b.inputProduct.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                       LOWER(b.inputProduct.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
                ORDER BY b.id DESC
            """)
    Page<InputBatch> searchBatches(@Param("searchTerm") String searchTerm, Pageable pageable);
}
