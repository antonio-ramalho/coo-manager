package com.manager.coopafi.purchaseModule.repositories;

import com.manager.coopafi.purchaseModule.entities.InputPurchase;
import com.manager.coopafi.purchaseModule.enums.PaymentStatus;
import com.manager.coopafi.purchaseModule.repositories.projections.TopSellingProductProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InputPurchaseRepository extends JpaRepository<InputPurchase, Long> {

  List<InputPurchase> findAllByStatus(PaymentStatus status);

  @Query("""
          SELECT p.id AS productId,
                 p.productName AS productName,
                 CAST(SUM(item.quantity.amount) AS double) AS totalQuantitySold,
                 CAST(p.measureUnit AS string) AS measureUnit
          FROM InputPurchaseItem item
          JOIN item.inputPurchase purchase
          JOIN item.inputBatch batch
          JOIN batch.inputProduct p
          WHERE purchase.status = com.manager.coopafi.purchaseModule.enums.PaymentStatus.PAID
            AND purchase.emissionDate >= :sinceDate
          GROUP BY p.id, p.productName, p.measureUnit
          ORDER BY SUM(item.quantity.amount) DESC
      """)
  List<TopSellingProductProjection> findTopSellingProducts(
      @Param("sinceDate") LocalDateTime sinceDate,
      Pageable pageable);
}
