package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.InputPurchase;
import com.manager.coopafi.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InputPurchaseRepository extends JpaRepository<InputPurchase, Long> {
    List<InputPurchase> findAllByStatus(PaymentStatus status);
}
