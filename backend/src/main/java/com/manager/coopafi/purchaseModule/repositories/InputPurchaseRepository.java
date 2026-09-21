package com.manager.coopafi.purchaseModule.repositories;

import com.manager.coopafi.purchaseModule.entities.InputPurchase;
import com.manager.coopafi.purchaseModule.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InputPurchaseRepository extends JpaRepository<InputPurchase, Long> {
    List<InputPurchase> findAllByStatus(PaymentStatus status);
}
