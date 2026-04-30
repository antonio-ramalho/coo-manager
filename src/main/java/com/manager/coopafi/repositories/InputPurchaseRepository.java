package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.InputPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputPurchaseRepository extends JpaRepository<InputPurchase, Long> {
}
