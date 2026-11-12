package com.manager.coomanager.purchaseModule.repositories;

import com.manager.coomanager.purchaseModule.entities.InputPurchase;
import com.manager.coomanager.purchaseModule.enums.PaymentStatus;
import com.manager.coomanager.purchaseModule.repositories.projections.TopSellingProductProjection;
import org.springframework.data.domain.Page;
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
          WHERE purchase.status = com.manager.coomanager.purchaseModule.enums.PaymentStatus.PAID
            AND purchase.emissionDate >= :sinceDate
          GROUP BY p.id, p.productName, p.measureUnit
          ORDER BY SUM(item.quantity.amount) DESC
      """)
  List<TopSellingProductProjection> findTopSellingProducts(
      @Param("sinceDate") LocalDateTime sinceDate,
      Pageable pageable);

  @Query("""
          SELECT COALESCE(AVG(p.totalValue.value), 0.0)
          FROM InputPurchase p
          WHERE p.status = com.manager.coomanager.purchaseModule.enums.PaymentStatus.PAID
      """)
  Double calculateAverageTicket();

  @Query("""
          SELECT p FROM InputPurchase p
          WHERE p.status = com.manager.coomanager.purchaseModule.enums.PaymentStatus.PAID
            AND p.emissionDate >= :sinceDate
          ORDER BY p.emissionDate ASC
      """)
  List<InputPurchase> findPaidPurchasesSince(@Param("sinceDate") LocalDateTime sinceDate);

  @Query(value = """
          SELECT DISTINCT p FROM InputPurchase p
          LEFT JOIN p.person person
          LEFT JOIN p.purchaseItems item
          LEFT JOIN item.inputBatch batch
          LEFT JOIN batch.inputProduct product
          WHERE (:searchTerm IS NULL OR :searchTerm = '' OR
                 (:codeTerm IS NOT NULL AND :codeTerm <> '' AND LOWER(CAST(p.uuid AS string)) LIKE LOWER(CONCAT('%', :codeTerm, '%'))) OR
                 LOWER(person.legalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                 TREAT(person AS NaturalPerson).cpf.cpfNumber LIKE CONCAT('%', :searchTerm, '%') OR
                 TREAT(person AS JuridicPerson).cnpj.cnpjNumber LIKE CONCAT('%', :searchTerm, '%') OR
                 LOWER(product.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                 LOWER(product.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                 LOWER(batch.batchCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
          ORDER BY p.id DESC
      """,
      countQuery = """
          SELECT COUNT(DISTINCT p) FROM InputPurchase p
          LEFT JOIN p.person person
          LEFT JOIN p.purchaseItems item
          LEFT JOIN item.inputBatch batch
          LEFT JOIN batch.inputProduct product
          WHERE (:searchTerm IS NULL OR :searchTerm = '' OR
                 (:codeTerm IS NOT NULL AND :codeTerm <> '' AND LOWER(CAST(p.uuid AS string)) LIKE LOWER(CONCAT('%', :codeTerm, '%'))) OR
                 LOWER(person.legalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                 TREAT(person AS NaturalPerson).cpf.cpfNumber LIKE CONCAT('%', :searchTerm, '%') OR
                 TREAT(person AS JuridicPerson).cnpj.cnpjNumber LIKE CONCAT('%', :searchTerm, '%') OR
                 LOWER(product.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                 LOWER(product.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                 LOWER(batch.batchCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
      """)
  Page<InputPurchase> searchPurchases(
      @Param("searchTerm") String searchTerm,
      @Param("codeTerm") String codeTerm,
      Pageable pageable);

  @Query("""
          SELECT p FROM InputPurchase p
          ORDER BY p.emissionDate DESC, p.id DESC
      """)
  List<InputPurchase> findRecentPurchases(Pageable pageable);
}
