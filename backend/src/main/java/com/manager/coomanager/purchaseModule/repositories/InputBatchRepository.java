package com.manager.coomanager.purchaseModule.repositories;

import com.manager.coomanager.purchaseModule.entities.InputBatch;
import com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus;
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
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.IN_STOCK,
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.LOW_STOCK
                )
            """)
    BigDecimal calculateTotalStockValue();

    @Query("SELECT COUNT(b) FROM InputBatch b WHERE b.productStatus = :status")
    long countByProductStatus(@Param("status") ProductInventoryStatus status);

    @Query("""
                SELECT b FROM InputBatch b
                WHERE b.productStatus IN (
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.IN_STOCK,
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.LOW_STOCK
                )
                AND b.expirationDate.value BETWEEN :startDate AND :endDate
                ORDER BY b.expirationDate.value ASC
            """)
    List<InputBatch> findExpiringBatches(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT b FROM InputBatch b
                WHERE b.productStatus <> com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.INACTIVE
                AND b.expirationDate.value >= :startDate
                AND b.expirationDate.value < :today
                ORDER BY b.expirationDate.value DESC
            """)
    List<InputBatch> findExpiredBatches(
            @Param("startDate") LocalDate startDate,
            @Param("today") LocalDate today);

    @Query("""
                SELECT b FROM InputBatch b
                WHERE b.productStatus IN (
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.IN_STOCK,
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.LOW_STOCK,
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.OUT_OF_STOCK
                )
                AND b.expirationDate.value < :today
            """)
    List<InputBatch> findUnmarkedExpiredBatches(@Param("today") LocalDate today);

    @Query("""
                SELECT COALESCE(SUM(b.currentQuantity.amount * b.inputProduct.productPrice.value), 0)
                FROM InputBatch b
                WHERE b.productStatus IN (
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.IN_STOCK,
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.LOW_STOCK
                )
                AND b.expirationDate.value <= :endDate
            """)
    BigDecimal calculateExpiringStockValue(@Param("endDate") LocalDate endDate);

    @Query("""
                SELECT b FROM InputBatch b
                WHERE (:onlyInStock = false OR b.productStatus IN (
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.IN_STOCK,
                    com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus.LOW_STOCK
                ))
                AND (:searchTerm IS NULL OR :searchTerm = '' OR
                     LOWER(b.batchCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                     LOWER(b.inputProduct.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                     LOWER(b.inputProduct.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
                ORDER BY b.id DESC
            """)
    Page<InputBatch> searchBatches(
            @Param("searchTerm") String searchTerm,
            @Param("onlyInStock") boolean onlyInStock,
            Pageable pageable);
}
